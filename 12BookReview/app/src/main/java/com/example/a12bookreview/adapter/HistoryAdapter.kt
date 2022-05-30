package com.example.a12bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a12bookreview.databinding.ItemHistoryBinding
import com.example.a12bookreview.model.History

class HistoryAdapter(val historyDeleteClickedListener: (String) -> Unit): ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {
    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.historyKeyWordTextView.text = history.keyword

            binding.historyKeywordDeleteButton.setOnClickListener {
                historyDeleteClickedListener(history.keyword.orEmpty())
            }
        }
    }

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}