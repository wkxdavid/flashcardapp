package edu.uw.ischool.scottng.memorymentor

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SelectedDateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_date)
        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val noteEditText = findViewById<EditText>(R.id.noteEditText)
        val scheduleButton = findViewById<Button>(R.id.scheduleButton)
        val selectedDate = intent.getStringExtra("selectedDate")
        dateTextView.text = selectedDate
        scheduleButton.setOnClickListener(View.OnClickListener { // Get the event note
            val note = noteEditText.text.toString().trim { it <= ' ' }
            if (note.isEmpty()) {
                Toast.makeText(this@SelectedDateActivity, "Enter Exam Name", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            Toast.makeText(this@SelectedDateActivity, "Event scheduled: $note", Toast.LENGTH_SHORT)
                .show()
        })
    }
}