package edu.uw.ischool.scottng.memorymentor

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class UserProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()

        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        val userDisplayNameTextView: TextView = findViewById(R.id.userDisplayNameTextView)
        val updateProfileButton: Button = findViewById(R.id.updateProfileButton)

        val user = auth.currentUser
        userEmailTextView.text = getString(R.string.user_email, user?.email ?: "No email")
        userDisplayNameTextView.text = getString(R.string.user_display_name, user?.displayName ?: "No display name")

        updateProfileButton.setOnClickListener {
            val user = auth.currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("USERNAME SET")
                .build()

            user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userDisplayNameTextView.text = getString(R.string.user_display_name, user.displayName)
                }
            }
        }
    }
}