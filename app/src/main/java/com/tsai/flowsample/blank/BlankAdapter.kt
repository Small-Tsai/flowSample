package com.tsai.flowsample.blank

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tsai.flowsample.databinding.ItemStringBinding
import androidx.recyclerview.widget.ListAdapter

class BlankAdapter : ListAdapter<String, BlankAdapter.BlankViewHolder>(DiffCallBack) {

    companion object DiffCallBack : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

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

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun getItem(position: Int): String {
        return super.getItem(position)
    }
}