package com.ahtabbasi.alarmmanagerpractice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Is triggered when alarm goes off, i.e. receiving a system broadcast
        if (intent.action == SHOW_NOTIFICATION_ACTION) {
            NotificationUtils.createNotificationChannel(context)
            NotificationUtils.showNotification(context)

            launchAlarm(context, false)
        }
    }

    companion object {
        private const val SHOW_NOTIFICATION_ACTION = "SHOW_NOTIFICATION_ACTION"
        private const val ALARM_INTERVAL_MS = 15 * 60 * 1000


        fun launchAlarm(context: Context, immediate: Boolean) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE)
                    as? AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                intent.action = SHOW_NOTIFICATION_ACTION
                PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                )
            }

            val triggerAfterMillis = if (immediate) 0 else ALARM_INTERVAL_MS

            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + triggerAfterMillis,
                alarmIntent
            )
        }


        fun isAlreadyLaunched(context: Context): Boolean {

            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            alarmIntent.action = SHOW_NOTIFICATION_ACTION

            return PendingIntent.getBroadcast(
                context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE
            ) != null
        }


        fun cancelAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE)
                    as? AlarmManager

            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            alarmIntent.action = SHOW_NOTIFICATION_ACTION

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                alarmIntent,
                PendingIntent.FLAG_NO_CREATE
            )

            if (pendingIntent != null && alarmManager != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }

        }
    }

}