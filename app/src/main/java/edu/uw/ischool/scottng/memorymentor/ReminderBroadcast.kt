package edu.uw.ischool.scottng.memorymentor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val note = intent?.getStringExtra("note") ?: "Time to study!"
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        val channelExists = notificationManager?.getNotificationChannel(CHANNEL_ID) != null
        if (!channelExists) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Study Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for study reminder"
            }
            notificationManager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Ensure this icon exists in your drawable folder
            .setContentTitle("Study Reminder")
            .setContentText(note)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "study_reminder_channel"
        const val NOTIFICATION_ID = 1
    }
}
