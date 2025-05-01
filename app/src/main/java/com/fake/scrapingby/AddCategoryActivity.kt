package com.fake.scrapingby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

//Activity for Creating Categories
class AddCategoryActivity : AppCompatActivity() {

    private lateinit var backArrowButton: ImageButton
    private lateinit var categoryNameTextEdit: EditText
    private lateinit var addCategoryButton: Button
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var categoryRepository: CategoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_category)

        backArrowButton = findViewById(R.id.backButton)
        categoryNameTextEdit = findViewById(R.id.categoryNameEditText)
        addCategoryButton = findViewById(R.id.saveCategoryButton)


        val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
        val currentuserId = sharedPref.getInt("loggedInUserId", -1)

        val db = AppDatabase.getInstance(this)
        categoryDAO = db.categoryDAO()
        categoryRepository = CategoryRepository(db.categoryDAO())

        //Creating the category and making sure all the fields are valid.
        addCategoryButton.setOnClickListener{
            val nameOfCategory = categoryNameTextEdit.text.toString()

            lifecycleScope.launch {
                //Checking if the name field is blank
                if(nameOfCategory.isBlank()){
                    Toast.makeText(this@AddCategoryActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                //Checking if the name has been used before.
                if(categoryRepository.isCategoryNameTaken(nameOfCategory, currentuserId)){
                    Toast.makeText(this@AddCategoryActivity, "Category already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val newcategory = Categories(userId = currentuserId, categoryName = nameOfCategory, categoryImage = "")
                    categoryRepository.addCategory(newcategory)
                    Toast.makeText(this@AddCategoryActivity, "Category Added", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        backArrowButton.setOnClickListener{
            finish()
        }
    }
}