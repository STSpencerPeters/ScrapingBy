package com.fake.scrapingby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var addCategoryButton: FloatingActionButton
    private lateinit var backArrowButton: ImageButton

    private lateinit var categoryRepo: CategoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category)

        recyclerView = findViewById(R.id.categoryRecyclerView)
        backArrowButton = findViewById(R.id.backButton)
        recyclerView.layoutManager = LinearLayoutManager(this)

        addCategoryButton = findViewById(R.id.addCategoryButton)

        val db = AppDatabase.getInstance(this)
        categoryDAO = db.categoryDAO()
        categoryRepo = CategoryRepository(db.categoryDAO())

        val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("loggedInUserId", -1)

        if(userId != -1){
            loadCategories(userId)
        }

        addCategoryButton.setOnClickListener{
            startActivity(Intent(this, AddCategoryActivity::class.java))
        }

        backArrowButton.setOnClickListener{
            startActivity(Intent(this, MenuDashboardActivity::class.java))
        }

    }

    private fun loadCategories(userId: Int){
        lifecycleScope.launch {
            val categories = categoryDAO.getCategoriesForUser(userId)
            val adapter = CategoryAdapter(categories)
            recyclerView.adapter = adapter
        }
    }
}