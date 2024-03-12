package edu.uw.ischool.scottng.memorymentor

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SelectedDateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_date)

        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val noteEditText = findViewById<EditText>(R.id.noteEditText)
        val scheduleButton = findViewById<Button>(R.id.scheduleButton)

        val selectedDate = intent.getStringExtra("selectedDate")
        dateTextView.text = selectedDate

        scheduleButton.setOnClickListener {
            val note = noteEditText.text.toString().trim()
            if (note.isEmpty()) {
                Toast.makeText(this, "Enter Exam Name", Toast.LENGTH_SHORT).show()
            } else {
                if (canScheduleExactAlarms() || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    scheduleNotifications(note, selectedDate ?: "")
                } else {
                    // Handle lack of permission here (e.g., show a dialog directing users to system settings)
                    Toast.makeText(this, "App does not have permission to schedule exact alarms.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun scheduleNotifications(note: String, selectedDate: String) {
        val dates = calculateNotificationTimes(selectedDate)
        Toast.makeText(this, "Notifications scheduled for $note", Toast.LENGTH_LONG).show()
        dates.forEach { date ->
            scheduleNotification(date, note)
        }
    }

    private fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else {
            true // Permission is automatically granted on controllers older than Android S.
        }
    }

    private fun scheduleNotification(time: Long, note: String) {
        val intent = Intent(applicationContext, ReminderBroadcast::class.java).apply {
            putExtra("note", note)
        }

        val requestCode = (time and 0xfffffff).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun calculateNotificationTimes(selectedDate: String): List<Long> {
        val parts = selectedDate.split("/").map { it.toInt() }
        val calendar = Calendar.getInstance().apply {
            set(parts[2], parts[0] - 1, parts[1], 8, 0) // Assuming format is YYYY/MM/DD and setting for 8 AM
        }

        return listOf(
            calendar.timeInMillis, // The day of the event
            calendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.timeInMillis, // One day before
            calendar.apply { add(Calendar.DAY_OF_MONTH, -3) }.timeInMillis // Three days before
        )
    }
}
