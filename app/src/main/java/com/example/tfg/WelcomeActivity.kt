package com.example.tfg

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.activity.ComponentActivity

class WelcomeActivity : ComponentActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var ivCircle: ImageView
    private lateinit var tvWelcome: TextView
    private lateinit var btPlay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        tvTitle = findViewById(R.id.tvTitle)
        ivCircle = findViewById(R.id.ivCircle)
        tvWelcome = findViewById(R.id.tvWelcome)
        btPlay = findViewById(R.id.btPlay)

        val email = intent.getStringExtra("USER_EMAIL") ?: "Usuario"

        tvWelcome.text = "Welcome, $email!"

        btPlay.setOnClickListener {
            showGame()
        }
    }

    private fun showGame() {
        val gameIntent = Intent(this, ClickerActivity::class.java)
        startActivity(gameIntent)
    }
}
