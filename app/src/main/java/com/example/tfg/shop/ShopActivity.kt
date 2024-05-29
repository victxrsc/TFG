package com.example.tfg.shop

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ShopActivity : AppCompatActivity() {

    private lateinit var tvShop: TextView
    private lateinit var tvSkins: TextView
    private lateinit var ivMonkey: ImageView
    private lateinit var ivRaccoon: ImageView
    private lateinit var ivKoala: ImageView
    private lateinit var tvMonkey: TextView
    private lateinit var tvRaccoon: TextView
    private lateinit var tvKoala: TextView
    private lateinit var tvPowerUps: TextView
    private lateinit var ivX2: ImageView
    private lateinit var ivX3: ImageView
    private lateinit var tvX2: TextView
    private lateinit var tvX3: TextView
    private lateinit var tvBackgrounds: TextView
    private lateinit var ivBackground1: ImageView
    private lateinit var ivBackground2: ImageView
    private lateinit var ivBackground3: ImageView
    private lateinit var tvBackground1: TextView
    private lateinit var tvBackground2: TextView
    private lateinit var tvBackground3: TextView
    private lateinit var tvPoints: TextView

    private var selectedImageView: ImageView? = null
    private val originalScale = 1.0f
    private val selectedScale = 1.2f

    // Firebase
    private val database = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        // Asociar vistas con variables
        tvShop = findViewById(R.id.tvShop)
        tvSkins = findViewById(R.id.tvSkins)
        ivMonkey = findViewById(R.id.ivMonkey)
        ivRaccoon = findViewById(R.id.ivRaccoon)
        ivKoala = findViewById(R.id.ivKoala)
        tvMonkey = findViewById(R.id.tvMonkey)
        tvRaccoon = findViewById(R.id.tvRaccoon)
        tvKoala = findViewById(R.id.tvKoala)
        tvPowerUps = findViewById(R.id.tvPowerUps)
        ivX2 = findViewById(R.id.ivX2)
        ivX3 = findViewById(R.id.ivX3)
        tvX2 = findViewById(R.id.tvX2)
        tvX3 = findViewById(R.id.tvX3)
        tvBackgrounds = findViewById(R.id.tvBackgrounds)
        ivBackground1 = findViewById(R.id.ivBackground1)
        ivBackground2 = findViewById(R.id.ivBackground2)
        ivBackground3 = findViewById(R.id.ivBackground3)
        tvBackground1 = findViewById(R.id.tvBackground1)
        tvBackground2 = findViewById(R.id.tvBackground2)
        tvBackground3 = findViewById(R.id.tvBackground3)
        tvPoints = findViewById(R.id.tvPoints)

        // Configurar listeners para los ImageView
        ivMonkey.setOnClickListener { handleSelection(ivMonkey) }
        ivRaccoon.setOnClickListener { handleSelection(ivRaccoon) }
        ivKoala.setOnClickListener { handleSelection(ivKoala) }
        ivX2.setOnClickListener { handleSelection(ivX2) }
        ivX3.setOnClickListener { handleSelection(ivX3) }
        ivBackground1.setOnClickListener { handleSelection(ivBackground1) }
        ivBackground2.setOnClickListener { handleSelection(ivBackground2) }
        ivBackground3.setOnClickListener { handleSelection(ivBackground3) }

        // Cargar los puntos del jugador
        loadPlayerPoints()
    }

    private fun handleSelection(imageView: ImageView) {
        // Restaurar tamaño original del ImageView previamente seleccionado
        selectedImageView?.scaleX = originalScale
        selectedImageView?.scaleY = originalScale

        // Agrandar el ImageView seleccionado
        imageView.scaleX = selectedScale
        imageView.scaleY = selectedScale

        // Guardar el ImageView seleccionado
        selectedImageView = imageView
    }

    private fun loadPlayerPoints() {
        // Obtiene el email del usuario actualmente autenticado
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        userEmail?.let { email ->
            // Obtiene la referencia al documento del jugador
            val userDocumentRef = database.collection("players").document(email)
            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    // Verifica si el documento existe y contiene el campo "totalPoints"
                    if (documentSnapshot.exists() && documentSnapshot.contains("totalPoints")) {
                        // Obtiene el valor de los puntos totales del jugador
                        val totalPoints = documentSnapshot.getLong("totalPoints")
                        totalPoints?.let {
                            // Actualiza el TextView con el valor de los puntos del jugador
                            tvPoints.text = "Points: $totalPoints"
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Maneja cualquier error al cargar los puntos del jugador
                    // Aquí puedes mostrar un mensaje de error o realizar otras acciones según sea necesario
                }
        }
    }
}
