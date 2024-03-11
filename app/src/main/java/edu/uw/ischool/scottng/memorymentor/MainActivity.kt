package edu.uw.ischool.scottng.memorymentor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {
    private lateinit var categoryBtn : Button
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_EMAIL_KEY = "USER_EMAIL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        val userEmail = intent.getStringExtra("user_email")
        saveEmailToPreferences(userEmail)

        categoryBtn = findViewById(R.id.btn_to_category)
        categoryBtn.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val calendarBtn = findViewById<Button>(R.id.calendarButton)
        calendarBtn.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
    }

    // Save email to SharedPreferences
    private fun saveEmailToPreferences(email: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_EMAIL_KEY, email)
        editor.apply()
    }

    // Retrieve email from SharedPreferences
    private fun getEmailFromPreferences(): String {
        return sharedPreferences.getString(PREF_EMAIL_KEY, "") ?: ""
    }
}
