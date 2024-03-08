package edu.uw.ischool.scottng.memorymentor

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth -> // Display the selected date
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar[year, month] = dayOfMonth
            val selectedDate = sdf.format(calendar.time)
            val intent = Intent(
                this@MainActivity,
                SelectedDateActivity::class.java
            )
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent)
        }
    }
}