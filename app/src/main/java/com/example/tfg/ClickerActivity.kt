package com.example.tfg

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

class ClickerActivity : ComponentActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btClick: ImageButton
    private lateinit var tvPoints: TextView
    private lateinit var ivPoints: ImageView
    private lateinit var btStart: Button
    private lateinit var tvTotalPoints: TextView

    private var pointsCount: Int = 0
    private var totalPoints: Int = 0
    private var timer: CountDownTimer? = null
    private var isTimerRunning: Boolean = false
    private var buttonPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicker)
        initComponent()
        initListeners()
    }

    private fun initComponent() {
        tvTitle = findViewById(R.id.tvTitle)
        tvTimer = findViewById(R.id.tvTimer)
        btClick = findViewById(R.id.btClick)
        tvPoints = findViewById(R.id.tvPoints)
        ivPoints = findViewById(R.id.ivPoints)
        btStart = findViewById(R.id.btStart)
        tvTotalPoints = findViewById(R.id.tvTotalPoints)
        tvTimer.text = "15"
        btClick.isEnabled = false
    }

    private fun initListeners() {
        btStart.setOnClickListener {
            startTimer()
            btClick.isEnabled = true
        }

        btClick.setOnTouchListener { _, event ->
            when (event.action) {
                //Actions while btClick is pressed
                MotionEvent.ACTION_DOWN -> {
                    //Count pressed time
                    buttonPressedTime = System.currentTimeMillis()
                    //Start runnable after 3 seconds
                    handler.postDelayed(longPressRunnable, 3000)
                    //Change Image to B al press and hold
                    btClick.setImageResource(R.drawable.monkey1b_icon)
                    //Increase Points one by one when the Image is touched
                    pointsCount++
                    //Update the points on the Text
                    tvPoints.text = pointsCount.toString()
                    true
                }
                //Actions while btClick is released
                MotionEvent.ACTION_UP -> {
                    //Stop the Runnable
                    handler.removeCallbacks(longPressRunnable)
                    val pressDuration = System.currentTimeMillis() - buttonPressedTime
                    //Change Image to A again after release the Click
                    btClick.setImageResource(R.drawable.monkey1a_icon)
                    true
                }
                else -> false
            }
        }
    }


    private val handler = Handler()
    private val longPressRunnable = object : Runnable {
        override fun run() {
            val pressDuration = System.currentTimeMillis() - buttonPressedTime
            //Change Image to C if pressed 3 seconds until pressed 6 seconds
            if (pressDuration >= 3000 && pressDuration < 6000) {
                btClick.setImageResource(R.drawable.monkey1c_icon)
                handler.postDelayed(this, 3000)

                //Change Image to D if pressed 6 seconds or more
            } else if (pressDuration >= 6000) {
                btClick.setImageResource(R.drawable.monkey1d_icon)
            }
        }
    }




    private fun startTimer() {
        pointsCount = 0
        tvPoints.text = "0"
        tvTimer.text = "15"
        timer?.cancel()
        timer = object : CountDownTimer(15000, 1000) {
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
            }
        }
        isTimerRunning = true
        timer?.start()
    }
}
