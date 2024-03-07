package edu.uw.ischool.scottng.memorymentor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity() {
    private lateinit var categories: List<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

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
    }
}
