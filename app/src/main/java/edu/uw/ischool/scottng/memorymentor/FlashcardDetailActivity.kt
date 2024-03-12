package edu.uw.ischool.scottng.memorymentor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class FlashcardDetailActivity : AppCompatActivity() {
    private lateinit var saveButton : Button
    private lateinit var deleteButton : Button
    private lateinit var flashcardRef : DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard_detail)

        val category = intent.getStringExtra("category")
        val question = intent.getStringExtra("question")
        val answer = intent.getStringExtra("answer")
        val key = intent.getStringExtra("key") ?: "Empty"

        // Get current user email
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")

        database = Firebase.database
        flashcardRef = database.getReference("Users/$userEmail/Categories/$category/$key/")

        val questionHolder: EditText = findViewById(R.id.et_question)
        questionHolder.setText(question)

        val answerHolder: EditText = findViewById(R.id.et_answer)
        answerHolder.setText(answer)

        saveButton = findViewById(R.id.btn_save)
        saveButton.setOnClickListener {
            val updatedQuest = questionHolder.text.toString().trim()
            val updatedAns = answerHolder.text.toString().trim()
            flashcardRef.setValue(Flashcard(updatedQuest, updatedAns, key))
            val intent = Intent(this, FlashcardActivity::class.java)
            intent.putExtra("category", category)
            startActivity(intent)
        }

        deleteButton = findViewById(R.id.btn_delete)
        deleteButton.setOnClickListener {
            flashcardRef.removeValue()
            val intent = Intent(this, FlashcardActivity::class.java)
            intent.putExtra("category", category)
            startActivity(intent)
        }

        // Set up the BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btn_to_category -> {
                    val intent = Intent(this, CategoryActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.calendarButton -> {
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}