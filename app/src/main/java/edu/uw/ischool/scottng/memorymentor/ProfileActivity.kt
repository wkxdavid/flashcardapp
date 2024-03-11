package edu.uw.ischool.scottng.memorymentor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import android.widget.ImageView

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()

        val usernameTextView: TextView = findViewById(R.id.usernameTextView)
        val profileIcon: ImageView = findViewById(R.id.profileIcon)

        val user = auth.currentUser

        user?.let {
            val userName = it.email ?: "No Name"
            usernameTextView.text = userName
            profileIcon.setImageResource(R.mipmap.ic_launcher_round)
        }

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            finish()
        }
    }
}
