package com.example.finalwmp

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.example.finalwmp.R
import android.widget.Toast
import java.util.*

class Stopwatch : AppCompatActivity() {
    // integers to store hours , minutes , seconds ,  ms
    var hours = 0
    var minutes = 0
    var secs = 0
    var ms = 0

    // integer to store seconds
    private var seconds = 0

    // boolean to check if stopwatch is running or not
    private var running = false

    // simple count variable to count number of laps
    var lapCount = 0

    // creating object of ImageView and Text View
    var playBtn: ImageView? = null
    var pauseBtn: ImageView? = null
    var stopBtn: ImageView? = null
    var timeLapseBtn: ImageView? = null
    var timeView: TextView? = null
    var timeViewms: TextView? = null
    var timeLapse: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)

        // initializing the Image view objects
        playBtn = findViewById<View>(R.id.playBtn) as ImageView
        pauseBtn = findViewById<View>(R.id.pauseBtn) as ImageView
        stopBtn = findViewById<View>(R.id.stopBtn) as ImageView
        timeLapseBtn = findViewById<View>(R.id.timeLapseBtn) as ImageView

        // initializing the text view objects
        timeView = findViewById<View>(R.id.time_view) as TextView
        timeViewms = findViewById<View>(R.id.time_view_ms) as TextView
        timeLapse = findViewById<View>(R.id.timeLapse) as TextView

        // play button click listener
        playBtn!!.setOnClickListener { //showing simple toast message to user
            Toast.makeText(this@Stopwatch, "Started", Toast.LENGTH_SHORT).show()

            // hide the play and stop button
            playBtn!!.visibility = View.GONE
            stopBtn!!.visibility = View.GONE

            // show the pause  and time lapse button
            pauseBtn!!.visibility = View.VISIBLE
            timeLapseBtn!!.visibility = View.VISIBLE

            // set running true
            running = true
        }
        // pause button click listener
        pauseBtn!!.setOnClickListener { //showing simple toast message to user
            Toast.makeText(this@Stopwatch, "Paused", Toast.LENGTH_SHORT).show()

            // show the play  and stop  button
            playBtn!!.visibility = View.VISIBLE
            stopBtn!!.visibility = View.VISIBLE

            // hide the pause  and time lapse button
            timeLapseBtn!!.visibility = View.GONE
            pauseBtn!!.visibility = View.GONE
            running = false
        }

        // stop  button click listener
        stopBtn!!.setOnClickListener { //showing simple toast message to user
            Toast.makeText(this@Stopwatch, "Stoped", Toast.LENGTH_SHORT).show()

            // set running to false
            running = false
            seconds = 0
            lapCount = 0

            // setting the text view to zero
            timeView!!.text = "00:00:00"
            timeViewms!!.text = "00"
            timeLapse!!.text = ""

            // show the play
            playBtn!!.visibility = View.VISIBLE

            // hide the pause , stop and time lapse button
            pauseBtn!!.visibility = View.GONE
            stopBtn!!.visibility = View.GONE
            timeLapseBtn!!.visibility = View.GONE
        }

        // lap button click listener
        timeLapseBtn!!.setOnClickListener { // calling timeLapse function
            timeLapseFun()
        }

        // calling runtimer
        runTimer()
    }

    private fun runTimer() {

        // creating handler
        val handlertime = Handler()

        // creating handler
        val handlerMs = Handler()
        handlertime.post(object : Runnable {
            override fun run() {
                hours = seconds / 3600
                minutes = seconds % 3600 / 60
                secs = seconds % 60

                // if running increment the seconds
                if (running) {
                    val time =
                        String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs)
                    timeView!!.text = time
                    seconds++
                }
                handlertime.postDelayed(this, 1000)
            }
        })
        handlerMs.post(object : Runnable {
            override fun run() {
                if (ms >= 99) {
                    ms = 0
                }

                // if running increment the ms
                if (running) {
                    val msString = String.format(Locale.getDefault(), "%02d", ms)
                    timeViewms!!.text = msString
                    ms++
                }
                handlerMs.postDelayed(this, 1)
            }
        })
    }

    fun timeLapseFun() {

        // increase lap count when function is called
        lapCount++
        var laptext = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs)
        val msString = String.format(Locale.getDefault(), "%02d", ms)

        // adding ms to lap text
        laptext = "$laptext:$msString"
        laptext = if (lapCount >= 10) {
            """ Lap $lapCount ------------->       $laptext 
 ${timeLapse!!.text}"""
        } else {
            """ Lap $lapCount --------------->       $laptext 
 ${timeLapse!!.text}"""
        }

        //showing simple toast message to user
        Toast.makeText(this@Stopwatch, "Lap $lapCount", Toast.LENGTH_SHORT).show()

        // showing the lap text
        timeLapse!!.text = laptext
    }
}