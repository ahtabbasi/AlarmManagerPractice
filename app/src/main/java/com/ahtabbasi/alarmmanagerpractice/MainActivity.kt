package com.ahtabbasi.alarmmanagerpractice

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    lateinit var permissionManager: ExactAlarmPermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionManager = ExactAlarmPermissionManager(
            context = this,
            success = {
                startAlarm()
            }, denied = {
                Toast.makeText(this, "Awwww :(", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onResume() {
        super.onResume()
        refreshStatus(null)
    }

    fun startAlarmClicked(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionManager.manageExactAlarmPermission()
        } else {
            startAlarm()
        }
    }

    private fun startAlarm() {
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