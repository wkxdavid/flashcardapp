package edu.uw.ischool.scottng.memorymentor

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
import android.widget.Button
import android.content.Intent
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.auth.FirebaseAuth
=======
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

>>>>>>> origin/non

class MainActivity : AppCompatActivity() {
    private lateinit var categoryBtn : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Firebase.database
        val myRef = database.getReference("message")

        categoryBtn = findViewById(R.id.btn_to_category)
        categoryBtn.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.registerButton).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
