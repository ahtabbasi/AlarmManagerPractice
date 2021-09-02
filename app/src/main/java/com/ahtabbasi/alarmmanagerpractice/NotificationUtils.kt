package com.ahtabbasi.alarmmanagerpractice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random
import android.media.RingtoneManager
import android.net.Uri


object NotificationUtils {

    private const val CHANNEL_ID = "TEST_NOTIFICATIONS"

    fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Test Notifications"
            val descriptionText = "These are some friendly test notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager? =
                ContextCompat.getSystemService(context, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context) {
        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.btn_star)
            .setContentTitle("AlarmManagerPractice")
            .setContentText("Party time -> " + getFormattedTime())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setSound(soundUri)
            .setDefaults(Notification.DEFAULT_SOUND)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            val uniqueId = Random(System.currentTimeMillis()).nextInt()
            notify(uniqueId, builder.build())
        }
    }

    private fun getFormattedTime(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }


}