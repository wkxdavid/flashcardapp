package edu.uw.ischool.scottng.memorymentor

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.database.database

class FlashcardDetailActivity : AppCompatActivity() {
    private lateinit var saveButton : Button
    private lateinit var deleteButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard_detail)

        val category = intent.getStringExtra("category")
        val question = intent.getStringExtra("question")
        val answer = intent.getStringExtra("answer")

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")

        val database = Firebase.database
        val categoryRef = database.getReference("Users/$userEmail/Categories/$category").push()

        val questionHolder: EditText = findViewById(R.id.et_question)
        questionHolder.setText(question)

        val answerHolder: EditText = findViewById(R.id.et_answer)
        answerHolder.setText(answer)

        saveButton = findViewById(R.id.btn_save)
        saveButton.setOnClickListener {
            val updatedQuest = questionHolder.text.toString()
            val updatedAns = answerHolder.text.toString()
            categoryRef.setValue(Flashcard(updatedQuest, updatedAns))
        }

        deleteButton = findViewById(R.id.btn_delete)
        deleteButton.setOnClickListener {
            categoryRef.removeValue()
            finish()
        }

    }
}