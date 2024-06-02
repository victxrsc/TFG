package com.example.tfg.shop

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ShopActivity : AppCompatActivity() {

    private lateinit var tvPoints: TextView
    private lateinit var tvShop: TextView
    private lateinit var tvSkins: TextView
    private lateinit var tvPowerUps: TextView
    private lateinit var tvBackgrounds: TextView

    private lateinit var ivMonkey: ImageView
    private lateinit var ivRaccoon: ImageView
    private lateinit var ivKoala: ImageView
    private lateinit var ivX2: ImageView
    private lateinit var ivX3: ImageView
    private lateinit var ivBackground1: ImageView
    private lateinit var ivBackground2: ImageView
    private lateinit var ivBackground3: ImageView

    private lateinit var tvMonkey: TextView
    private lateinit var tvRaccoon: TextView
    private lateinit var tvKoala: TextView
    private lateinit var tvX2: TextView
    private lateinit var tvX3: TextView
    private lateinit var tvBackground1: TextView
    private lateinit var tvBackground2: TextView
    private lateinit var tvBackground3: TextView

    private lateinit var btBuy: Button

    private var updatedPoints: Long? = null
    private var selectedImageView: ImageView? = null
    private val database = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        initComponent()
        initListeners()

        loadPlayerData()
    }

    private fun initComponent() {
        tvPoints = findViewById(R.id.tvPoints)
        tvShop = findViewById(R.id.tvShop)
        tvSkins = findViewById(R.id.tvSkins)
        tvPowerUps = findViewById(R.id.tvPowerUps)
        tvBackgrounds = findViewById(R.id.tvBackgrounds)

        ivMonkey = findViewById(R.id.ivMonkey)
        ivRaccoon = findViewById(R.id.ivRaccoon)
        ivKoala = findViewById(R.id.ivKoala)
        ivX2 = findViewById(R.id.ivX2)
        ivX3 = findViewById(R.id.ivX3)
        ivBackground1 = findViewById(R.id.ivBackground1)
        ivBackground2 = findViewById(R.id.ivBackground2)
        ivBackground3 = findViewById(R.id.ivBackground3)

        tvMonkey = findViewById(R.id.tvMonkey)
        tvRaccoon = findViewById(R.id.tvRaccoon)
        tvKoala = findViewById(R.id.tvKoala)
        tvX2 = findViewById(R.id.tvX2)
        tvX3 = findViewById(R.id.tvX3)
        tvBackground1 = findViewById(R.id.tvBackground1)
        tvBackground2 = findViewById(R.id.tvBackground2)
        tvBackground3 = findViewById(R.id.tvBackground3)

        btBuy = findViewById(R.id.btBuy)
    }

    private fun initListeners() {
        ivMonkey.setOnClickListener { handleSelection(ivMonkey) }
        ivRaccoon.setOnClickListener { handleSelection(ivRaccoon) }
        ivKoala.setOnClickListener { handleSelection(ivKoala) }
        ivX2.setOnClickListener { handleSelection(ivX2) }
        ivX3.setOnClickListener { handleSelection(ivX3) }
        ivBackground1.setOnClickListener { handleSelection(ivBackground1) }
        ivBackground2.setOnClickListener { handleSelection(ivBackground2) }
        ivBackground3.setOnClickListener { handleSelection(ivBackground3) }

        btBuy.setOnClickListener { handlePurchase() }
    }

    private fun loadPlayerData() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        userEmail?.let { email ->
            val userDocumentRef = database.collection("players").document(email)
            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val totalPoints = documentSnapshot.getLong("totalPoints")
                        totalPoints?.let {
                            tvPoints.text = "Points: $it"
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun handleSelection(imageView: ImageView) {
        selectedImageView?.scaleX = 1.0f
        selectedImageView?.scaleY = 1.0f

        imageView.scaleX = 1.2f
        imageView.scaleY = 1.2f

        selectedImageView = imageView
    }

    private fun handlePurchase() {
        val selectedImageView = selectedImageView ?: run {
            Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show()
            return
        }

        val priceString = selectedImageView.tag as? String ?: run {
            Toast.makeText(this, "Invalid item price", Toast.LENGTH_SHORT).show()
            return
        }

        val price = if (priceString == "Free") 0 else priceString.toLongOrNull() ?: run {
            Toast.makeText(this, "Invalid item price format", Toast.LENGTH_SHORT).show()
            return
        }

        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        userEmail?.let { email ->
            val userDocumentRef = database.collection("players").document(email)
            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists() && documentSnapshot.contains("totalPoints")) {
                        val totalPoints = documentSnapshot.getLong("totalPoints") ?: return@addOnSuccessListener

                        if (totalPoints >= price) {
                            val newTotalPoints = totalPoints - price
                            val currentItems = documentSnapshot.get("items") as? ArrayList<String> ?: arrayListOf()

                            currentItems.add(getItemName(selectedImageView.id))

                            userDocumentRef.update(
                                mapOf(
                                    "totalPoints" to newTotalPoints,
                                    "items" to currentItems
                                )
                            ).addOnSuccessListener {
                                tvPoints.text = "Points: $newTotalPoints"
                                updatedPoints = newTotalPoints
                                Toast.makeText(this, "Purchase successful", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Not enough points", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getItemName(imageViewId: Int): String {
        return when (imageViewId) {
            R.id.ivMonkey -> "Monkey"
            R.id.ivRaccoon -> "Raccoon"
            R.id.ivKoala -> "Koala"
            R.id.ivX2 -> "X2 PowerUp"
            R.id.ivX3 -> "X3 PowerUp"
            R.id.ivBackground1 -> "Background1"
            R.id.ivBackground2 -> "Background2"
            R.id.ivBackground3 -> "Background3"
            else -> ""
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        updatedPoints?.let {
            intent.putExtra("updatedPoints", it)
        }
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }
}