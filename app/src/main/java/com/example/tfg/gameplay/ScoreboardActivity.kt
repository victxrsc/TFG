package com.example.tfg.gameplay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.scoreboards.PlayerScore
import com.example.tfg.scoreboards.ScoreboardAdapter
import com.google.firebase.firestore.FirebaseFirestore

class ScoreboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var scoreboardAdapter: ScoreboardAdapter
    private val database = FirebaseFirestore.getInstance()
    private val playerScores = mutableListOf<PlayerScore>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        recyclerView = findViewById(R.id.rvScoreboards)
        recyclerView.layoutManager = LinearLayoutManager(this)
        scoreboardAdapter = ScoreboardAdapter(playerScores)
        recyclerView.adapter = scoreboardAdapter

        loadPlayerScores()
    }

    private fun loadPlayerScores() {
        database.collection("players")
            .orderBy("record", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                playerScores.clear()
                for (document in result) {
                    val email = document.id
                    val score = document.getLong("record")?.toInt() ?: 0
                    playerScores.add(PlayerScore(email, score))
                }
                scoreboardAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting scores: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
