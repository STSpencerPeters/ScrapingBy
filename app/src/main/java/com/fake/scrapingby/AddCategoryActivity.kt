package com.fake.scrapingby

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var changeImageButton: Button
    private lateinit var categoryImageView: ImageView
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    private lateinit var categoryDAO: CategoryDAO
    private lateinit var categoryRepository: CategoryRepository

    private var selectedImageUri: Uri? = null // Store picked image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_category)

        // Initialize views
        backArrowButton = findViewById(R.id.backButton)
        categoryNameTextEdit = findViewById(R.id.categoryNameEditText)
        addCategoryButton = findViewById(R.id.saveCategoryButton)
        changeImageButton = findViewById(R.id.pickImageButton)
        categoryImageView = findViewById(R.id.categoryImageView)

        val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
        val currentuserId = sharedPref.getInt("loggedInUserId", -1)

        val db = AppDatabase.getInstance(this)
        categoryDAO = db.categoryDAO()
        categoryRepository = CategoryRepository(categoryDAO)

        // Setup image picker
        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let { uri ->
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    selectedImageUri = uri
                    categoryImageView.setImageURI(uri)
                }
            }
        }

        // Launch picker
        changeImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            pickImageLauncher.launch(intent)
        }

        // Add category logic
        addCategoryButton.setOnClickListener {
            val nameOfCategory = categoryNameTextEdit.text.toString()

            lifecycleScope.launch {
                if (nameOfCategory.isBlank()) {
                    Toast.makeText(this@AddCategoryActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (categoryRepository.isCategoryNameTaken(nameOfCategory, currentuserId)) {
                    Toast.makeText(this@AddCategoryActivity, "Category already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val imageUriString = selectedImageUri?.toString() ?: ""
                    val newCategory = Categories(
                        userId = currentuserId,
                        categoryName = nameOfCategory,
                        categoryImage = imageUriString
                    )
                    categoryRepository.addCategory(newCategory)
                    Toast.makeText(this@AddCategoryActivity, "Category Added", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        backArrowButton.setOnClickListener {
            finish()
        }
    }
}
