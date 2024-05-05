package com.example.tfg

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : ComponentActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var ivCircle: ImageView
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var btSignIn: Button
    private lateinit var btLogIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        tvTitle = findViewById(R.id.tvTitle)
        ivCircle = findViewById(R.id.ivCircle)
        etEmail = findViewById(R.id.etEmail)
        etPass = findViewById(R.id.etPass)
        btSignIn = findViewById(R.id.btSignIn)
        btLogIn = findViewById(R.id.btLogIn)

        setup()
    }

    private fun setup() {
        btSignIn.setOnClickListener {
            if (etEmail.text.isNotEmpty() && etPass.text.isNotEmpty()) {
                val email = etEmail.text.toString()
                val password = etPass.text.toString()

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showWelcome(email)
                        } else {
                            showAlert()
                        }
                    }
            } else {
                showAlert("The fields cannot be empty")
            }
        }

        btLogIn.setOnClickListener {
            if (etEmail.text.isNotEmpty() && etPass.text.toString().isNotEmpty()) {
                val email = etEmail.text.toString()
                val password = etPass.text.toString()

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showGame()
                        } else {
                            showAlert("Log In Error")
                        }
                    }
            } else {
                showAlert("The fields cannot be empty")
            }
        }
    }

    private fun showAlert(message: String = "Authentication Error") {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showWelcome(email: String) {
        val welcomeIntent = Intent(this, WelcomeActivity::class.java)
        welcomeIntent.putExtra("USER_EMAIL", email)
        startActivity(welcomeIntent)
    }

    private fun showGame() {
        val gameIntent = Intent(this, ClickerActivity::class.java)
        startActivity(gameIntent)
    }
}
