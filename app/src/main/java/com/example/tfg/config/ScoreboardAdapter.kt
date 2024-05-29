package com.example.tfg.scoreboards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R

class ScoreboardAdapter(private val playerScores: List<PlayerScore>) :
    RecyclerView.Adapter<ScoreboardAdapter.ScoreboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_scoreboard, parent, false)
        return ScoreboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreboardViewHolder, position: Int) {
        val playerScore = playerScores[position]
        holder.tvPlayerName.text = playerScore.name
        holder.tvPlayerScore.text = playerScore.score.toString()
    }

    override fun getItemCount(): Int = playerScores.size

    class ScoreboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlayerName: TextView = itemView.findViewById(R.id.tvPlayerName)
        val tvPlayerScore: TextView = itemView.findViewById(R.id.tvPlayerScore)
    }
}
