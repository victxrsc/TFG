package com.example.tfg.gameplay

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.os.Handler
import com.example.tfg.R
import com.example.tfg.config.OptionsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ClickerActivity : ComponentActivity() {
    private lateinit var tvMonkeyClicker: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvPoints: TextView
    private lateinit var tvTotalPoints: TextView
    private lateinit var btClick: ImageButton
    private lateinit var ivPoints: ImageView
    private lateinit var ivOptions: ImageView
    private lateinit var ivShop: ImageView
    private lateinit var btStart: Button

    private var pointsCount: Int = 0
    private var totalPoints: Int = 0
    private var record: Int = 0
    private var timer: CountDownTimer? = null
    private var isTimerRunning: Boolean = false
    private var buttonPressedTime: Long = 0
    private var gameDurationInSeconds: Int = 15

    private val database = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicker)
        initComponent()
        initListeners()
        loadPlayerPoints()


        val selectedTimer = intent.getIntExtra("selectedTimer", 15)
        gameDurationInSeconds = selectedTimer
        tvTimer.text = gameDurationInSeconds.toString()
    }

    private fun initComponent() {
        tvMonkeyClicker = findViewById(R.id.tvMonkeyClicker)
        tvTimer = findViewById(R.id.tvTimer)
        tvPoints = findViewById(R.id.tvPoints)
        tvTotalPoints = findViewById(R.id.tvTotalPoints)
        tvTimer.text = gameDurationInSeconds.toString()
        btClick = findViewById(R.id.btClick)
        btStart = findViewById(R.id.btStart)
        btClick.isEnabled = false
        ivPoints = findViewById(R.id.ivPoints)
        ivOptions = findViewById(R.id.ivOptions)
        ivShop = findViewById(R.id.ivShop)
    }

    private fun initListeners() {
        btStart.setOnClickListener {
            startTimer()
            btClick.isEnabled = true
        }

        btClick.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    buttonPressedTime = System.currentTimeMillis()
                    handler.postDelayed(longPressRunnable, 3000)
                    btClick.setImageResource(R.drawable.monkey1b_icon)
                    pointsCount++
                    tvPoints.text = pointsCount.toString()
                    true
                }

                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacks(longPressRunnable)
                    btClick.setImageResource(R.drawable.monkey1a_icon)
                    true
                }

                else -> false
            }
        }
        ivOptions.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            startActivity(intent)
        }
        ivShop.setOnClickListener {
            ivShop.setOnClickListener {
                val intent = Intent(this, ShopActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun updateTimer(selectedDuration: Int) {
        gameDurationInSeconds = selectedDuration
        tvTimer.text = gameDurationInSeconds.toString()
    }

    private val handler = Handler()
    private val longPressRunnable = object : Runnable {
        override fun run() {
            val pressDuration = System.currentTimeMillis() - buttonPressedTime
            if (pressDuration >= 3000 && pressDuration < 6000) {
                btClick.setImageResource(R.drawable.monkey1c_icon)
                handler.postDelayed(this, 3000)
            } else if (pressDuration >= 6000) {
                btClick.setImageResource(R.drawable.monkey1d_icon)
            }
        }
    }

    private fun startTimer() {
        pointsCount = 0
        tvPoints.text = "0"
        tvTimer.text = gameDurationInSeconds.toString()
        timer?.cancel()
        timer = object : CountDownTimer(gameDurationInSeconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                if (seconds > 0) {
                    tvTimer.text = seconds.toString()
                } else {
                    tvTimer.text = "0"
                }
            }

            override fun onFinish() {
                isTimerRunning = false
                btClick.isEnabled = false
                totalPoints += pointsCount
                btClick.setImageResource(R.drawable.monkey1a_icon)
                tvTotalPoints.text = "Total Points: $totalPoints"
                storePoints(totalPoints)
                updateGamesPlayed()

                // Verificar si la puntuación de la última partida supera el récord
                if (pointsCount > record) {
                    record = pointsCount // Actualizar el récord
                }
            }
        }
        isTimerRunning = true
        timer?.start()
    }

    private fun storePoints(points: Int) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        userEmail?.let { email ->
            val userDocumentRef = database.collection("players").document(email)
            userDocumentRef.update("totalPoints", points)
        }
    }

    // En updateRecordInDatabase
    private fun updateRecordInDatabase(record: Int) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        userEmail?.let { email ->
            val userDocumentRef = database.collection("players").document(email)
            val recordData = hashMapOf(
                "record" to record,
            )
            userDocumentRef.update(recordData as Map<String, Any>)
        }
    }

    // En loadPlayerPoints
    private fun loadPlayerPoints() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        userEmail?.let { email ->
            val userDocumentRef = database.collection("players").document(email)
            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists() && documentSnapshot.contains("totalPoints")) {
                        val totalPoints = documentSnapshot.getLong("totalPoints")
                        totalPoints?.let {
                            this.totalPoints = it.toInt()
                            tvTotalPoints.text = "Total Points: $totalPoints"
                        }
                    }

                    // Cargar el récord y la modalidad si existen en el documento
                    if (documentSnapshot.exists() && documentSnapshot.contains("record")) {
                        val record = documentSnapshot.getLong("record")
                        val modeOrdinal = documentSnapshot.getLong("mode")
                        record?.let {
                            this.record = it.toInt()
                        }

                    }
                }
        }
    }

    private fun updateGamesPlayed() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        userEmail?.let { email ->
            val userDocumentRef = database.collection("players").document(email)
            userDocumentRef.update("gamesPlayed", FieldValue.increment(1))
        }
    }
}
