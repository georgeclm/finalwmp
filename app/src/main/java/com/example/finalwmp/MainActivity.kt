package com.example.finalwmp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalwmp.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity()
{
    // add view binding in build gradle module to bind view to activity
    private lateinit var binding : ActivityMainBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        // add the timer service in the android manifest so the timer will run in background
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startStopButton.setOnClickListener { startStopTimer() }
        binding.resetButton.setOnClickListener { resetTimer() }
        binding.changePage.setOnClickListener{
            val intent = Intent(this, ForTimer::class.java)
            startActivity(intent)
        }
        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
    }

    private fun resetTimer()
    {
        stopTimer()
        // set the time to 0
        time = 0.0
        // update the time
        binding.timeTV.text = getTimeStringFromDouble(time)
    }

    private fun startStopTimer()
    {
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer()
    {
        // run the timer service with the time
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        // change the button text to stop and the icon
        binding.startStopButton.text = getString(R.string.Stop)
        binding.startStopButton.icon = getDrawable(R.drawable.ic_baseline_pause_24)
        // update the bool
        timerStarted = true
    }

    private fun stopTimer()
    {
        // stop the service
        stopService(serviceIntent)
        // change the button text to start and the icon
        binding.startStopButton.text = getString(R.string.Start)
        binding.startStopButton.icon = getDrawable(R.drawable.ic_baseline_play_arrow_24)
        // update the bool
        timerStarted = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            binding.timeTV.text = getTimeStringFromDouble(time)
        }
    }
    // this is to take the time to change the format to hours minutes and second
    private fun getTimeStringFromDouble(time: Double): String
    {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }
    // Set the format to make it better in textview
    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hour, min, sec)
}