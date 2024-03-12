package edu.uw.ischool.scottng.memorymentor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class CategoryActivity : AppCompatActivity() {
    private lateinit var homeBtn : Button
    private lateinit var calendarBtn : Button
    private lateinit var profileBtn : Button
    private lateinit var addCategoryEt: EditText
    private lateinit var addCategoryBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_EMAIL_KEY = "USER_EMAIL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // get current user email
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        val userEmail = intent.getStringExtra("user_email")
        saveEmailToPreferences(userEmail)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val database = Firebase.database
        val userRef = database.getReference("Users/$userEmail/")
        val categoryRef = database.getReference("Users/$userEmail/Categories/")

        // get list of Category objects
        var categoryNames = mutableListOf<String>()
        userRef.child("Categories").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryNames.clear()
                for (categorySnap in snapshot.children) {
                    categorySnap.key?.let { categoryNames.add(it) }
                }
                // set recycler view
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

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
        
    }

    // Save email to SharedPreferences
    private fun saveEmailToPreferences(email: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_EMAIL_KEY, email)
        editor.apply()
    }

}
