package edu.uw.ischool.scottng.memorymentor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class FlashcardDetailActivity : AppCompatActivity() {
    private lateinit var saveButton : Button
    private lateinit var deleteButton : Button
    private lateinit var flashcardRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard_detail)

        val category = intent.getStringExtra("category")
        val question = intent.getStringExtra("question")
        val answer = intent.getStringExtra("answer")
        val key = intent.getStringExtra("key") ?: "Empty"

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")

        val database = Firebase.database
        flashcardRef = database.getReference("Users/$userEmail/Categories/$category/$key/")

        val questionHolder: EditText = findViewById(R.id.et_question)
        questionHolder.setText(question)

        val answerHolder: EditText = findViewById(R.id.et_answer)
        answerHolder.setText(answer)

        saveButton = findViewById(R.id.btn_save)
        saveButton.setOnClickListener {
            val updatedQuest = questionHolder.text.toString()
            val updatedAns = answerHolder.text.toString()
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

    }
}