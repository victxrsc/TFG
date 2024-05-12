package com.example.tfg.config

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.tfg.gameplay.ClickerActivity
import com.example.tfg.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

enum class ProviderType {
    EMAIL,
    GOOGLE
}

@Suppress("DEPRECATION")
class WelcomeActivity : ComponentActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var ivCircle: ImageView
    private lateinit var tvWelcome: TextView
    private lateinit var tvProvider: TextView
    private lateinit var btPlay: Button
    private lateinit var btLogOut: Button

    private val database = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        tvTitle = findViewById(R.id.tvTitle)
        ivCircle = findViewById(R.id.ivCircle)
        tvWelcome = findViewById(R.id.tvWelcome)
        tvProvider = findViewById(R.id.tvProvider)
        btPlay = findViewById(R.id.btPlay)
        btLogOut = findViewById(R.id.btLogOut)

        // Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        setup(email ?: "", provider ?: "")

        // Data Storing
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        // Database Storing
        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        userEmail?.let { email ->
            val userDocumentRef = database.collection("players").document(email)
            userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    database.collection("players").document(email).set(
                        hashMapOf(
                            "provider" to provider,
                            "totalPoints" to 0,
                            "record" to 0,
                            "gamesPlayed" to 0
                        )
                    )
                }
            }
        }
    }

    private fun setup(email: String, provider: String) {
        title = "Welcome"
        tvWelcome.text = "Welcome $email"
        tvProvider.text = "Provider used $provider"
        btLogOut.setOnClickListener {
            //Data Removing
            val prefs =
                getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
        btPlay.setOnClickListener {
            val intent = Intent(this, ClickerActivity::class.java)
            startActivity(intent)
        }
    }
}
