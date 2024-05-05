package com.example.tfg

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        tvTitle = findViewById(R.id.tvTitle)
        ivCircle = findViewById(R.id.ivCircle)
        tvWelcome = findViewById(R.id.tvWelcome)
        tvProvider = findViewById(R.id.tvProvider)
        btPlay = findViewById(R.id.btPlay)
        btLogOut = findViewById(R.id.btLogOut)

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        setup(email?:"", provider?: "")

        //Data Storing
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

    }

    private fun setup(email: String, provider: String) {
        title = "Welcome"
        tvWelcome.text = "Welcome $email"
        tvProvider.text = "Provider used $provider"
        btLogOut.setOnClickListener{
            //Data Removing
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
        btPlay.setOnClickListener{
            val intent = Intent(this, ClickerActivity::class.java)
            startActivity(intent)
        }
    }
}
