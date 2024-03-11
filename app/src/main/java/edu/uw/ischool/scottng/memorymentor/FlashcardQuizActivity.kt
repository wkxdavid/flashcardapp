package edu.uw.ischool.scottng.memorymentor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class FlashcardQuizActivity : AppCompatActivity() {
    private lateinit var backButton : Button
    private lateinit var nextButton : Button
    private lateinit var questionText : TextView
    private lateinit var flashcards : List<Flashcard>
    private var displayingQuestion = true
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard_quiz)

        // get flashcards from a given category name (change to firebase fetch later)
        val categoryName = intent.getStringExtra("category")
        val categoryObj = (application as FlashcardApp).categoryRepository.getCategories().find { it.name == categoryName }

        flashcards = categoryObj!!.flashcards

        questionText = findViewById(R.id.txt_question)
        questionText.setOnClickListener {
            displayingQuestion = !displayingQuestion
            displayQuestion(currentQuestionIndex, flashcards, questionText)
        }

        displayQuestion(currentQuestionIndex, flashcards, questionText)

        backButton = findViewById(R.id.btn_back_flashcard)
        nextButton = findViewById(R.id.btn_next_flashcard)

        backButton.setOnClickListener {
            if(currentQuestionIndex === 0) {
                val intent = Intent(this, FlashcardActivity::class.java)
                intent.putExtra("category", categoryName)
                startActivity(intent)
            } else {
                currentQuestionIndex--
                displayingQuestion = true
                displayQuestion(currentQuestionIndex, categoryObj.flashcards, questionText)
            }
        }

        nextButton.setOnClickListener {
            if(currentQuestionIndex === flashcards.size - 1) {
                val intent = Intent(this, FlashcardActivity::class.java)
                intent.putExtra("category", categoryName)
                startActivity(intent)
            } else {
                currentQuestionIndex++
                displayingQuestion = true
                displayQuestion(currentQuestionIndex, categoryObj.flashcards, questionText)
            }
        }
    }

    fun displayQuestion(currentQuestionIndex: Int, flashcards: List<Flashcard>, questionText: TextView) {
        // Display the question text
        if (displayingQuestion) {
            questionText.text = flashcards[currentQuestionIndex].question
        } else {
            questionText.text = flashcards[currentQuestionIndex].answer
        }
    }
}