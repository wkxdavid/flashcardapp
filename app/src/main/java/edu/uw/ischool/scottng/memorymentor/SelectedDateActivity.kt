package edu.uw.ischool.scottng.memorymentor

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
                    Toast.makeText(this, "App does not have permission to schedule exact alarms.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun scheduleNotifications(note: String, selectedDate: String) {
        val dates = calculateNotificationTimes(selectedDate)
        dates.forEach { date ->
            scheduleNotification(date, note)
        }
    }

    private fun canScheduleExactAlarms(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            return alarmManager.canScheduleExactAlarms()
        }
        return true
    }

    private fun scheduleNotification(time: Long, note: String) {
        val intent = Intent(applicationContext, ReminderBroadcast::class.java).also {
            it.putExtra("note", note)
        }

        val requestCode = (time and 0xfffffff).toInt()
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, requestCode, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun calculateNotificationTimes(selectedDate: String): List<Long> {
        // Assuming the selectedDate format is "yyyy/MM/dd"
        val parts = selectedDate.split("/").map { it.toInt() }
        val calendar = Calendar.getInstance().apply {
            set(parts[0], parts[1] - 1, parts[2], 22, 3) // Setting for 10:03 PM on the selected date
        }

        return listOf(
            calendar.timeInMillis, // The day of the event
            calendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.timeInMillis, // One day before
            calendar.apply { add(Calendar.DAY_OF_MONTH, -3) }.timeInMillis // Three days before
        )
    }

}
