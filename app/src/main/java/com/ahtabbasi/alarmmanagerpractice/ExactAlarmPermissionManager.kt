package com.ahtabbasi.alarmmanagerpractice

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog


class ExactAlarmPermissionManager(
    private val context: Context,
    val success: () -> Unit,
    val denied: () -> Unit,
) {

    @RequiresApi(Build.VERSION_CODES.S)
    fun manageExactAlarmPermission() {
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        when {
            alarmManager.canScheduleExactAlarms() -> success()
            else -> showPermissionRationale()

        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun openExactAlarmSettings() {
        Intent().apply {
            action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
        }.also {
            context.startActivity(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showPermissionRationale() {
        AlertDialog.Builder(context)
            .setMessage("This app needs alarm permission because of XYZ reason. Please allow from settings.")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                openExactAlarmSettings()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                denied()
            }.show()
    }


}