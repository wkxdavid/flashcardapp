package edu.uw.ischool.scottng.memorymentor

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SelectedDateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_date)
        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val noteEditText = findViewById<EditText>(R.id.noteEditText)
        val intent = intent
        if (intent != null && intent.hasExtra("selectedDate")) {
            val selectedDate = intent.getStringExtra("selectedDate")
            dateTextView.text = selectedDate
        }
    }
}
