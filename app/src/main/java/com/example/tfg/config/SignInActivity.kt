package com.example.tfg.config

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.tfg.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


@Suppress("DEPRECATION")
class SignInActivity : ComponentActivity() {
    private val GOOGLE_SIGN_IN = 100

    private lateinit var tvTitle: TextView
    private lateinit var ivCircle: ImageView
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var btSignIn: Button
    private lateinit var btLogIn: Button
    private lateinit var btGoogle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        tvTitle = findViewById(R.id.tvTitle)
        ivCircle = findViewById(R.id.ivCircle)
        etEmail = findViewById(R.id.etEmail)
        etPass = findViewById(R.id.etPass)
        btSignIn = findViewById(R.id.btSignIn)
        btLogIn = findViewById(R.id.btLogIn)
        btGoogle = findViewById(R.id.btGoogle)


        //Setup
        setup()
        session()

    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)
        if (email != null && provider != null) {
            showWelcome(email, ProviderType.valueOf(provider))
        }
    }

    private fun setup() {
        title = "Authentication"

        btSignIn.setOnClickListener {
            if (etEmail.text.isNotEmpty() && etPass.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(etEmail.text.toString(), etPass.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showWelcome(it.result?.user?.email ?: "", ProviderType.EMAIL)
                        } else {
                            showAlert()
                        }
                    }
            }
        }
        btLogIn.setOnClickListener {
            if (etEmail.text.isNotEmpty() && etPass.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(etEmail.text.toString(), etPass.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showWelcome(it.result?.user?.email ?: "", ProviderType.EMAIL)
                        } else {
                            showAlert()
                        }
                    }
            }
        }
        btGoogle.setOnClickListener {
            //Config
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Authentication Error")
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showWelcome(email: String, provider: ProviderType) {
        val welcomeIntent = Intent(this, WelcomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(welcomeIntent)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showWelcome(account.email ?: "", ProviderType.GOOGLE)
                            } else {
                                showAlert()
                            }
                        }

                }
            } catch (e: ApiException) {
                showAlert()
            }
        }
    }
}
