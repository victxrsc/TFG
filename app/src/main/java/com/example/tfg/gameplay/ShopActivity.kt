package com.example.tfg.gameplay

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.tfg.R
import android.widget.TextView
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

class ShopActivity : AppCompatActivity() {
    private lateinit var cvSkins: CardView
    private lateinit var cvPowerUps: CardView
    private lateinit var cvBackgrounds: CardView

    private lateinit var tvShop: TextView
    private lateinit var tvSkins: TextView
    private lateinit var ivMonkey: ImageView
    private lateinit var ivRaccoon: ImageView
    private lateinit var ivKoala: ImageView
    private lateinit var tvMonkey: TextView
    private lateinit var tvRaccoon: TextView
    private lateinit var tvKoala: TextView
    private lateinit var ivCoinRaccoon: ImageView
    private lateinit var ivCoinKoala: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop)

        cvSkins = findViewById(R.id.cvSkins)
        cvPowerUps = findViewById(R.id.cvPowerUps)
        cvBackgrounds = findViewById(R.id.cvBackgrounds)

        tvShop = findViewById(R.id.tvShop)
        tvSkins = findViewById(R.id.tvSkins)
        ivMonkey = findViewById(R.id.ivMonkey)
        ivRaccoon = findViewById(R.id.ivRaccoon)
        ivKoala = findViewById(R.id.ivKoala)
        tvMonkey = findViewById(R.id.tvMonkey)
        tvRaccoon = findViewById(R.id.tvRaccoon)
        tvKoala = findViewById(R.id.tvKoala)
        ivCoinRaccoon = findViewById(R.id.ivCoinRaccoon)
        ivCoinKoala = findViewById(R.id.ivCoinKoala)

        cvSkins.setOnClickListener { showSkinsSection() }
        cvPowerUps.setOnClickListener { showPowerUpsSection() }
        cvBackgrounds.setOnClickListener { showBackgroundsSection() }

        showSkinsSection()
    }

    private fun showSkinsSection() {
        tvShop.text = "Skins"
        setViewsVisibility(visibility = ConstraintLayout.VISIBLE)
    }

    private fun showPowerUpsSection() {
        tvShop.text = "Power Ups"
        setViewsVisibility(visibility = ConstraintLayout.GONE)
    }

    private fun showBackgroundsSection() {
        tvShop.text = "Backgrounds"
        setViewsVisibility(visibility = ConstraintLayout.GONE)
    }

    private fun setViewsVisibility(visibility: Int) {
        tvSkins.visibility = visibility
        ivMonkey.visibility = visibility
        ivRaccoon.visibility = visibility
        ivKoala.visibility = visibility
        tvMonkey.visibility = visibility
        tvRaccoon.visibility = visibility
        tvKoala.visibility = visibility
        ivCoinRaccoon.visibility = visibility
        ivCoinKoala.visibility = visibility
    }
}
