package com.tsai.flowsample.blank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.tsai.flowsample.databinding.ItemStringBinding

class TestAdapter : PartialDiffAdapter<String, TestAdapter.BlankViewHolder>(10) {

    class BlankViewHolder(private val binding: ItemStringBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.string = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlankViewHolder {
        return BlankViewHolder(
            ItemStringBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: BlankViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun setupDiffResult(
        oldSubList: List<String>,
        newSubList: List<String>,
    ): DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldSubList.size
        }

        override fun getNewListSize(): Int {
            return newSubList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldSubList[oldItemPosition] == newSubList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldSubList[oldItemPosition] == newSubList[newItemPosition]
        }
    })
}