package com.ahtabbasi.alarmmanagerpractice

import android.app.PendingIntent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        refreshStatus(null)
    }

    fun startAlarmClicked(view: View) {
        Toast.makeText(this, "Launching alarm!", Toast.LENGTH_LONG).show()
        AlarmReceiver.launchAlarm(this, true)
        refreshStatus(null)
    }

    fun cancelAlarm(view: View) {
        Toast.makeText(this, "Canceling alarm!", Toast.LENGTH_LONG).show()
        AlarmReceiver.cancelAlarm(this)
        refreshStatus(null)
    }

    fun refreshStatus(view: View?) {
        val tvAlarmStatus = findViewById<TextView>(R.id.tvAlarmStatus)
        val launched = AlarmReceiver.isAlreadyLaunched(this)
        tvAlarmStatus.text = if (launched) "Alarm is currently active" else "No active alarm"
    }


}