package edu.uw.ischool.scottng.memorymentor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class CategoryActivity : AppCompatActivity() {
    private lateinit var addCategoryEt: EditText
    private lateinit var addCategoryBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Get current user email
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Get firebase database ref
        database = Firebase.database
        val userRef = database.getReference("Users/$userEmail/")
        val categoryRef = database.getReference("Users/$userEmail/Categories/")

        // Get list of Category objects
        var categoryNames = mutableListOf<String>()
        userRef.child("Categories").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryNames.clear()
                for (categorySnap in snapshot.children) {
                    categorySnap.key?.let { categoryNames.add(it) }
                }
                // set recycler view for list of categories
                val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
                val adaptor = CategoryAdapter(categoryNames)
                recyclerView.layoutManager = GridLayoutManager(this@CategoryActivity, 2)
                recyclerView.adapter = adaptor

                adaptor.setOnClickListener(object :
                    CategoryAdapter.OnClickListener {
                    override fun onClick(position: Int, category: String) {
                        val intent = Intent(this@CategoryActivity, FlashcardActivity::class.java)
                        intent.putExtra("category", category)
                        startActivity(intent)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Error", "Failed to read value.", error.toException())
            }
        })

        addCategoryEt = findViewById(R.id.et_add_category)
        addCategoryBtn = findViewById(R.id.btn_add_category)

        addCategoryBtn.setOnClickListener{
            val categoryTitle = addCategoryEt.text.toString().trim()
            if (categoryTitle.isNotBlank()) {
                val newCategoryRef = categoryRef.child(categoryTitle).push()
                val newCategoryKey = newCategoryRef.key ?: "Empty"

                val flashcard = Flashcard("Empty", "Empty", newCategoryKey)
                newCategoryRef.setValue(flashcard)
            }
        }

        // Set up the BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btn_to_category -> {
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
