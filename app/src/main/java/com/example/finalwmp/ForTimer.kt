package com.example.finalwmp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.finalwmp.databinding.ActivityForTimerBinding

class ForTimer : AppCompatActivity() {

    private lateinit var binding : ActivityForTimerBinding
    var START_MILLI_SECONDS = 60000L

    lateinit var countdown_timer: CountDownTimer
    var isRunning: Boolean = false;
    var fromPause: Boolean = false;
    var time_in_milli_seconds = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val actionBar = supportActionBar
        actionBar!!.title = "Timer"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding.button.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                if(!fromPause){
                    val time  = binding.timeEditText.text.toString()
                    time_in_milli_seconds = time.toLong() *60000L
                }
                startTimer(time_in_milli_seconds)
            }
        }

        binding.reset.setOnClickListener {
            resetTimer()
        }


    }

    private fun pauseTimer() {

        binding.button.text = "Start"
        countdown_timer.cancel()
        isRunning = false
        fromPause = true
        binding.reset.visibility = View.VISIBLE
    }

    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                binding.finishText.text = "Finish!"
                binding.reset.visibility = View.VISIBLE
                binding.button.visibility = View.INVISIBLE
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI()
            }
        }
        countdown_timer.start()

        isRunning = true
        binding.button.text = "Pause"
        binding.reset.visibility = View.INVISIBLE

    }

    private fun resetTimer() {
        binding.finishText.text = ""
        time_in_milli_seconds = START_MILLI_SECONDS
        updateTextUI()
        binding.reset.visibility = View.INVISIBLE
        binding.button.text = "Start"
        binding.button.visibility = View.VISIBLE
        isRunning = false

    }

    private fun updateTextUI() {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60

        binding.timer.text = "$minute:$seconds"
    }
}