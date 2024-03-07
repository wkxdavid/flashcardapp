package edu.uw.ischool.scottng.memorymentor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FlashcardActivity : AppCompatActivity() {
    private lateinit var categories: List<Category>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        // get flashcards from a given category name (change to firebase fetch later)
        val categoryName = intent.getStringExtra("category")
        categories = (application as FlashcardApp).categoryRepository.getCategories()
        val categoryObj = categories.find { it.name == categoryName }

        val flashcards = categoryObj!!.flashcards

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_flashcards)
        toolbar.title = categoryName
        setSupportActionBar(toolbar)

        val recyclerView : RecyclerView = findViewById(R.id.flashcard_recyclerView)
        val adaptor = FlashcardAdapter(flashcards)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adaptor

        adaptor.setOnClickListener(object :
            FlashcardAdapter.OnClickListener {
            override fun onClick(position: Int, flashcard: Flashcard) {
                val intent = Intent(this@FlashcardActivity, FlashcardDetailActivity::class.java)
                intent.putExtra("question", flashcard.question)
                intent.putExtra("answer", flashcard.answer)
                startActivity(intent)
            }
        })
    }
}
