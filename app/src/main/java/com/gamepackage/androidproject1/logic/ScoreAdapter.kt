package com.gamepackage.androidproject1.logic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gamepackage.androidproject1.R
import com.gamepackage.androidproject1.databinding.ItemScoreBinding

class ScoreAdapter(
    private val scores: List<Score>,
    private val clickListener: (Score) -> Unit
) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    inner class ScoreViewHolder(val binding: ItemScoreBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]

        // Set rank number (position + 1)
        holder.binding.itemScoreRank.text = (position + 1).toString()

        // Set player name and score
        holder.binding.itemScorePlayerName.text = score.playerName
        holder.binding.itemScoreValue.text = score.score.toString()

        // Color the top 3 ranks with special colors
        when (position) {
            0 -> { // Gold
                holder.binding.root.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.gold))
            }
            1 -> { // Silver
                holder.binding.root.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.silver))
            }
            2 -> { // Bronze
                holder.binding.root.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.bronze))
            }
            else -> { // Default background
                holder.binding.root.setBackgroundResource(R.drawable.item_background)
            }
        }

        // Handle click
        holder.binding.root.setOnClickListener {
            clickListener(score)
        }
    }

    override fun getItemCount(): Int = scores.size
}