package edu.uw.ischool.scottng.memorymentor

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userName: TextView = findViewById(R.id.userName)
        val userEmail: TextView = findViewById(R.id.userEmail)
        val flashcardStats: TextView = findViewById(R.id.flashcardStats)

        userName.text = "John Doe"
        userEmail.text = "johndoe@example.com"
        flashcardStats.text = "Flashcards Sets: 10"

    }
}
