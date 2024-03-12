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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager?.getNotificationChannel(CHANNEL_ID) == null) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, "Study Reminder", importance).apply {
                    description = "Channel for study reminder"
                }
                notificationManager?.createNotificationChannel(channel)
            }
        }

        val notificationBuilder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Study Reminder")
            .setContentText(note)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationId = System.currentTimeMillis().toInt() // Generates a unique ID for each notification
        notificationManager?.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        const val CHANNEL_ID = "study_reminder_channel"
    }
}
