package com.tsai.flowsample.blank

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

abstract class PartialDiffAdapter<T, VH : RecyclerView.ViewHolder>(
    private val diffOffset: Int = 10,
) : RecyclerView.Adapter<VH>() {

    private var oldItems: List<T> = emptyList()
    private var newItems: List<T> = emptyList()
    private var visibleStartIndex = 0
    private var visibleEndIndex = 0

    private val oldDiffStartIndex
        get() = (visibleStartIndex - diffOffset).takeIf { it >= 0 } ?: 0

    private val oldDiffEndIndex
        get() = (visibleEndIndex + diffOffset).takeIf { it <= oldItems.size } ?: oldItems.size

    private val oldSubList
        get() = oldItems.subList(oldDiffStartIndex, oldDiffEndIndex)

    private val newDiffStartIndex
        get() = (visibleStartIndex - diffOffset).takeIf { it >= 0 } ?: 0

    private val newDiffEndIndex
        get() = (visibleEndIndex + diffOffset).takeIf { it <= newItems.size } ?: newItems.size

    private val newSubList
        get() = newItems.subList(newDiffStartIndex, newDiffEndIndex)

    private var hasNotifiedListCountUpdated = false


    abstract fun setupDiffResult(
        oldSubList: List<T> = this.oldSubList,
        newSubList: List<T> = this.newSubList
    ): DiffUtil.DiffResult

    @JvmName("submitList1")
    fun submitList(
        list: List<T>,
        visibleStartIndex: Int,
        visibleEndIndex: Int
    ) {
        this.newItems = list
        this.visibleStartIndex = visibleStartIndex
        this.visibleEndIndex = visibleEndIndex

        setupDiffResult().dispatchUpdatesTo(setupListUpdateCallBack())

        if (hasNotifiedListCountUpdated.not() && oldItems.size != oldItems.size) {
            this.notifyItemRangeChanged(oldDiffEndIndex, 1)
        }

        this.oldItems = list
    }


    fun getItem(position: Int): T {
        return oldItems[position]
    }

    private fun setupListUpdateCallBack(): ListUpdateCallback {
        val defaultListUpdateCallback: ListUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                val realPosition = oldDiffStartIndex + position
                this@PartialDiffAdapter.notifyItemRangeInserted(realPosition, count)
                hasNotifiedListCountUpdated = true
            }

            override fun onRemoved(position: Int, count: Int) {
                val realPosition = oldDiffStartIndex + position
                this@PartialDiffAdapter.notifyItemRangeRemoved(realPosition, count)
                hasNotifiedListCountUpdated = true
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                val realFromPosition = oldDiffStartIndex + fromPosition
                val realToPosition = oldDiffStartIndex + toPosition
                this@PartialDiffAdapter.notifyItemMoved(realFromPosition, realToPosition)
                hasNotifiedListCountUpdated = true
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                val realPosition = oldDiffStartIndex + position
                this@PartialDiffAdapter.notifyItemRangeChanged(realPosition, count, payload)
                hasNotifiedListCountUpdated = true
            }
        }
        return defaultListUpdateCallback
    }

    override fun getItemCount(): Int {
        return oldItems.size
    }
}