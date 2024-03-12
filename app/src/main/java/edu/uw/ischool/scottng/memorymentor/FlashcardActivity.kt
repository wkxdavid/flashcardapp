package edu.uw.ischool.scottng.memorymentor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class FlashcardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        // get current user email
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")

        // get flashcards from a given category name (change to firebase fetch later)
        val category = intent.getStringExtra("category")
        val database = Firebase.database
        val flashcardsRef = database.getReference("Users/$userEmail/Categories/$category")

        val flashcards = mutableListOf<Flashcard>()
        flashcardsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                flashcards.clear()
                for (flashcardSnap in snapshot.children) {
                    val flashcardKey = flashcardSnap.key ?: "Empty"
                    var answer = ""
                    var question = ""
                    for (subSnapshot in flashcardSnap.children) {
                        val subKey = subSnapshot.key
                        val subValue = subSnapshot.value
                        if(subKey == "answer") {
                            answer = subValue.toString()
                        } else {
                            question = subValue.toString()
                        }
                    }
                    val flashcard = Flashcard(question, answer, flashcardKey)
                    flashcards.add(flashcard)
                }
                val recyclerView : RecyclerView = findViewById(R.id.flashcard_recyclerView)
                val adaptor = FlashcardAdapter(flashcards)
                recyclerView.layoutManager = GridLayoutManager(this@FlashcardActivity, 2)
                recyclerView.adapter = adaptor

                adaptor.setOnClickListener(object :
                    FlashcardAdapter.OnClickListener {
                    override fun onClick(position: Int, flashcard: Flashcard) {
                        val intent = Intent(this@FlashcardActivity, FlashcardDetailActivity::class.java)
                        intent.putExtra("category", category)
                        intent.putExtra("question", flashcard.question)
                        intent.putExtra("answer", flashcard.answer)
                        intent.putExtra("key", flashcard.key)
                        startActivity(intent)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Error", "Failed to read value.", error.toException())
            }
        })



        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_flashcards)
        toolbar.title = category
        setSupportActionBar(toolbar)

        val addButton: Button = findViewById(R.id.btn_add_flashcards)
        addButton.setOnClickListener{
            val newFlashcardRef = flashcardsRef.child("").push()
            val newFlashcardKey = newFlashcardRef.key ?: "Empty"
            val intent = Intent(this, FlashcardDetailActivity::class.java)
            intent.putExtra("category", category)
            intent.putExtra("question", "")
            intent.putExtra("answer", "")
            intent.putExtra("key", newFlashcardKey)
            startActivity(intent)
        }

        val deleteButton: Button = findViewById(R.id.btn_delete_flashcards)
        deleteButton.setOnClickListener{
            flashcardsRef.removeValue()
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("category", category)
            startActivity(intent)
        }

        val startButton: Button = findViewById(R.id.btn_start_flashcards)
        startButton.setOnClickListener{
            val intent = Intent(this, FlashcardQuizActivity::class.java)
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