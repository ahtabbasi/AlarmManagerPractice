package com.ahtabbasi.alarmmanagerpractice

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun makeSureBatteryOptimizationIsIgnored() {
        val intent = Intent()
        val packageName = packageName
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            showOptimizationRationale {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }

    private fun showOptimizationRationale(positiveAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setMessage("This app needs you to disable battery optimization to continue.")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                positiveAction()
            }.show()
    }

    override fun onResume() {
        super.onResume()
        makeSureBatteryOptimizationIsIgnored()
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