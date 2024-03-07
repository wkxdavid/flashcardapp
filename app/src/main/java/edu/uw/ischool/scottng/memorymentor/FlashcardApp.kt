package edu.uw.ischool.scottng.memorymentor

import android.app.Application

// Domain objects
data class Category(
    val name: String,
    val flashcards: List<Flashcard>
)

data class Flashcard(val question: String, val answer: String)


// Repository
interface ICategoryRepository {
    fun getCategories(): List<Category>
}

class CategoryRepository : ICategoryRepository {
    private val allCategories: List<Category> = listOf(
        Category(
            "Mathematics",
            listOf(
                Flashcard("What is the value of pi (π)?", "3.14159"),
                Flashcard("Solve for x: 2x + 5 = 15", "x = 5"))
            ),
        Category(
            "Science",
            listOf(Flashcard("What is the chemical symbol for water?", "3.14159"), Flashcard("Name the four fundamental forces in physics.", "Gravity, Electromagnetism, Weak Nuclear Force, Strong Nuclear Force")),
        ),
        Category(
            "Programming",
            listOf(
                Flashcard("What does HTML stand for?", "Hypertext Markup Language"),
                Flashcard("Name a popular programming language for machine learning.", "Python"))
            )
    )

    override fun getCategories(): List<Category> {
        return allCategories
    }
}

class FlashcardApp : Application() {
    lateinit var categoryRepository: ICategoryRepository

    override fun onCreate() {
        super.onCreate()
        categoryRepository = CategoryRepository()
    }
}
