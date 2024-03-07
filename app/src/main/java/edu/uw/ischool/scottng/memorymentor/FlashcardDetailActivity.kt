package edu.uw.ischool.scottng.memorymentor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class FlashcardDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard_detail)

        val question = intent.getStringExtra("question")
        val answer = intent.getStringExtra("answer")

        val question_holder: EditText = findViewById(R.id.et_question)
        question_holder.setText(question)

        val answer_holder: EditText = findViewById(R.id.et_answer)
        answer_holder.setText(answer)
    }
}