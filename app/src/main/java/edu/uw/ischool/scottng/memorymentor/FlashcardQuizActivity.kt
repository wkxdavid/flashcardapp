package edu.uw.ischool.scottng.memorymentor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase

class FlashcardQuizActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionText: TextView
    private lateinit var flashcards: MutableList<Flashcard>
    private var displayingQuestion = true
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard_quiz)

        val category = intent.getStringExtra("category")

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")
        val flashcardsRef = FirebaseDatabase.getInstance().getReference("Users/$userEmail/Categories/$category")

        flashcardsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                flashcards = mutableListOf<Flashcard>()
                for (flashcardSnap in snapshot.children) {
                    val flashcardKey = flashcardSnap.key ?: "Empty"
                    var answer = ""
                    var question = ""
                    for (subSnapshot in flashcardSnap.children) {
                        val subKey = subSnapshot.key
                        val subValue = subSnapshot.value
                        if (subKey == "answer") {
                            answer = subValue.toString()
                        } else {
                            question = subValue.toString()
                        }
                    }
                    val flashcard = Flashcard(question, answer, flashcardKey)
                    flashcards.add(flashcard)

                    if (!flashcards.isNullOrEmpty()) {
                        displayQuestion(currentQuestionIndex, flashcards, questionText)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Error", "Failed to read value.", error.toException())
            }
        })

        questionText = findViewById(R.id.txt_question)
        questionText.setOnClickListener {
            displayingQuestion = !displayingQuestion
            displayQuestion(currentQuestionIndex, flashcards, questionText)
        }

        backButton = findViewById(R.id.btn_back_flashcard)
        nextButton = findViewById(R.id.btn_next_flashcard)

        backButton.setOnClickListener {
            if(currentQuestionIndex === 0) {
                val intent = Intent(this, FlashcardActivity::class.java)
                intent.putExtra("category", category)
                startActivity(intent)
            } else {
                currentQuestionIndex--
                displayingQuestion = true
                displayQuestion(currentQuestionIndex, flashcards, questionText)
            }
        }

        nextButton.setOnClickListener {
            if(currentQuestionIndex === flashcards.size - 1) {
                val intent = Intent(this, FlashcardActivity::class.java)
                intent.putExtra("category", category)
                startActivity(intent)
            } else {
                currentQuestionIndex++
                displayingQuestion = true
                displayQuestion(currentQuestionIndex, flashcards, questionText)
            }
        }
    }

    private fun displayQuestion(currentQuestionIndex: Int, flashcards: List<Flashcard>, questionText: TextView) {
        // Display the question text
        if (displayingQuestion) {
            questionText.text = flashcards[currentQuestionIndex].question
        } else {
            questionText.text = flashcards[currentQuestionIndex].answer
        }
    }
}
