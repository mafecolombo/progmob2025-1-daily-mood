package com.android.dailymood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.dailymood.data.local.model.MoodEntry
import com.android.dailymood.databinding.ItemMoodHistoryBinding

class MoodHistoryAdapter(private val entries: List<MoodEntry>) :
    RecyclerView.Adapter<MoodHistoryAdapter.MoodViewHolder>() {

    inner class MoodViewHolder(val binding: ItemMoodHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMoodHistoryBinding.inflate(inflater, parent, false)
        return MoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val entry = entries[position]
        holder.binding.tvDate.text = "Data: ${entry.date}"
        holder.binding.tvMood.text = "Humor: ${entry.emoji} (Escala: ${entry.moodScale})"
        holder.binding.tvDescription.text =
            if (entry.description.isNullOrBlank()) "(Sem descrição)" else entry.description
    }

    override fun getItemCount() = entries.size
}
