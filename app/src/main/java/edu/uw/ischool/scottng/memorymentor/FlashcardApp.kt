package edu.uw.ischool.scottng.memorymentor

import android.app.Application
import android.nfc.Tag
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

// Domain objects
data class Category(
    val name: String,
    val flashcards: List<Flashcard>
)

data class Flashcard(val question: String, val answer: String, val key: String)


// Repository
interface ICategoryRepository {
    fun getCategories(): List<Category>
}

class CategoryRepository : ICategoryRepository {
    private var userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    override fun getCategories(): List<Category> {
        userEmail = userEmail.replace(".", "_")
        var categories = mutableListOf<Category>()
        val database = Firebase.database
        val categoryRef = database.getReference("Users/$userEmail/Categories")

        categoryRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (categorySnap in snapshot.children) {
                    val title = categorySnap.key
                    val flashcards = categorySnap.child("flashcards").getValue(object : GenericTypeIndicator<List<Flashcard>>() {})
                    val category = title?.let { Category(it, flashcards ?: listOf(Flashcard("Empty", "Empty", "Empty"))) }
                    category?.let { categories.add(it)}
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Erroe", "Failed to read value.", error.toException())
            }

        })

        return categories;
    }
}

class FlashcardApp : Application() {
    lateinit var categoryRepository: ICategoryRepository

    override fun onCreate() {
        super.onCreate()
        categoryRepository = CategoryRepository()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
