package edu.uw.ischool.scottng.memorymentor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.database

class CategoryActivity : AppCompatActivity() {
    private lateinit var categories: List<Category>
    private lateinit var addCategoryEt: EditText
    private lateinit var addCategoryBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // get current user email
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", "")

        val database = Firebase.database
        val categoryRef = database.getReference("Users/$userEmail/Categories")

        // get list of Category objects
        categories = (application as FlashcardApp).categoryRepository.getCategories()
        val categoryNames = categories.map { it.name }

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        val adaptor = CategoryAdapter(categoryNames)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adaptor

        adaptor.setOnClickListener(object :
            CategoryAdapter.OnClickListener {
            override fun onClick(position: Int, category: String) {
                val intent = Intent(this@CategoryActivity, FlashcardActivity::class.java)
                intent.putExtra("category", category)
                startActivity(intent)
            }
        })

        addCategoryEt = findViewById(R.id.et_add_category)
        addCategoryBtn = findViewById(R.id.btn_add_category)

        addCategoryBtn.setOnClickListener{
            val categoryTitle = addCategoryEt.text.toString().trim()
            if(categoryTitle.isNotBlank()) {
                val newCategoryRef = categoryRef.child(categoryTitle)
                newCategoryRef.setValue("")
            }
        }
    }
}
