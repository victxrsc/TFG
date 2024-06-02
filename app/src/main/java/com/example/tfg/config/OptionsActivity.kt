package com.example.tfg.config

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.tfg.R
import com.example.tfg.gameplay.ClickerActivity
import com.example.tfg.gameplay.ScoreboardActivity

class OptionsActivity : AppCompatActivity() {
    private lateinit var tv5Segs: TextView
    private lateinit var tv10Segs: TextView
    private lateinit var tv15Segs: TextView
    private lateinit var btScoreboards: Button
    private lateinit var btApply: Button

    private var selectedTextView: TextView? = null
    private val originalScale = 1.0f
    private val selectedScale = 1.35f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        tv5Segs = findViewById(R.id.tv5Segs)
        tv10Segs = findViewById(R.id.tv10Segs)
        tv15Segs = findViewById(R.id.tv15Segs)
        btScoreboards = findViewById(R.id.btScoreboards)
        btApply = findViewById(R.id.btApply)

        handleSelection(tv15Segs)

        tv5Segs.setOnClickListener { handleSelection(tv5Segs) }
        tv10Segs.setOnClickListener { handleSelection(tv10Segs) }
        tv15Segs.setOnClickListener { handleSelection(tv15Segs) }

        btApply.setOnClickListener {
            val intent = Intent(this, ClickerActivity::class.java)
            val selectedTimer = when (selectedTextView) {
                tv5Segs -> 5
                tv10Segs -> 10
                else -> 15
            }
            intent.putExtra("selectedTimer", selectedTimer)
            startActivity(intent)
        }
        btScoreboards.setOnClickListener {
            val intent = Intent(this, ScoreboardActivity::class.java)
            startActivity(intent)
        }

    }

    private fun handleSelection(textView: TextView) {
        selectedTextView?.apply {
            scaleX = originalScale
            scaleY = originalScale
        }

        textView.scaleX = selectedScale
        textView.scaleY = selectedScale

        selectedTextView = textView
    }
}
