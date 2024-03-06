package edu.uw.ischool.scottng.memorymentor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.Firebase
import com.google.firebase.database.database

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello World!")

        val startQuizButton = findViewById<Button>(R.id.startQuizButton)
        val createQuizButton = findViewById<Button>(R.id.createQuizButton)
        startQuizButton.setOnClickListener {
            val intent = Intent(this, StartQuizActivity::class.java)
            startActivity(intent)
        }

        createQuizButton.setOnClickListener {
            val intent = Intent(this, CreateQuizActivity::class.java)
            startActivity(intent)        }
    }
}