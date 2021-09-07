package com.ahtabbasi.alarmmanagerpractice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import java.util.*

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
        private const val ALARM_INTERVAL_MS = 15 * 60 * 1000L


        fun launchAlarm(context: Context, immediate: Boolean) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE)
                    as? AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                intent.action = SHOW_NOTIFICATION_ACTION
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

            val triggerAfterMillis = if (immediate) 0 else ALARM_INTERVAL_MS
            val currentTime = Calendar.getInstance().time.time

            val alarmClockInfo = AlarmManager.AlarmClockInfo(
                currentTime + triggerAfterMillis,
                alarmIntent
            )
            alarmManager?.setAlarmClock(
                alarmClockInfo,
                alarmIntent
            )

            // enabling this so that alarm will continue after restart
            enableAlarmAtBoot(context)
        }


        fun isAlreadyLaunched(context: Context): Boolean {

            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            alarmIntent.action = SHOW_NOTIFICATION_ACTION

            return PendingIntent.getBroadcast(
                context,
                0,
                alarmIntent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
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
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )

            if (pendingIntent != null && alarmManager != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }

            disableAlarmAtBoot(context)
        }


        private fun enableAlarmAtBoot(context: Context) {
            val receiver = ComponentName(context, SampleBootReceiver::class.java)

            context.packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }


        private fun disableAlarmAtBoot(context: Context) {
            val receiver = ComponentName(context, SampleBootReceiver::class.java)

            context.packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

}