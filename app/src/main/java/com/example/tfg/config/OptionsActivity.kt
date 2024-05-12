package com.example.tfg.config

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.tfg.R


class OptionsActivity : AppCompatActivity() {
    private lateinit var tv5Segs: TextView
    private lateinit var tv10Segs: TextView
    private lateinit var tv15Segs: TextView
    private lateinit var btApply: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        tv5Segs = findViewById(R.id.tv5Segs)
        tv10Segs = findViewById(R.id.tv10Segs)
        tv15Segs = findViewById(R.id.tv15Segs)
        btApply = findViewById(R.id.btApply)

        // Establecer el color predeterminado de tv15Segs a rojo
        tv15Segs.setTextColor(android.graphics.Color.RED)

        // Definir el OnClickListener para tv5Segs
        tv5Segs.setOnClickListener {
            tv5Segs.setTextColor(android.graphics.Color.RED)
            tv10Segs.setTextColor(android.graphics.Color.WHITE)
            tv15Segs.setTextColor(android.graphics.Color.WHITE)
        }

        // Definir el OnClickListener para tv10Segs
        tv10Segs.setOnClickListener {
            tv5Segs.setTextColor(android.graphics.Color.WHITE)
            tv10Segs.setTextColor(android.graphics.Color.RED)
            tv15Segs.setTextColor(android.graphics.Color.WHITE)
        }

        // Definir el OnClickListener para tv15Segs
        tv15Segs.setOnClickListener {
            tv5Segs.setTextColor(android.graphics.Color.WHITE)
            tv10Segs.setTextColor(android.graphics.Color.WHITE)
            tv15Segs.setTextColor(android.graphics.Color.RED)
        }
    }
}