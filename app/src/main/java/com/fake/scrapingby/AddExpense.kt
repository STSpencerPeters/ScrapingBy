package com.fake.scrapingby

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.text.DecimalFormat
import android.icu.util.CurrencyAmount
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class AddExpense : AppCompatActivity() {

    private lateinit var expenseRepository: ExpenseRepository
    private lateinit var bottomNavBarView: BottomNavigationView
    private lateinit var textAmount: EditText
    private lateinit var textDescription: EditText
    private lateinit var monthDropdown: AutoCompleteTextView
    private lateinit var selectedMonth: String
    private lateinit var textYear: EditText
    private lateinit var categoryDropdown: AutoCompleteTextView
    private lateinit var imagePreview: ImageView
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
    private var selectedCategoryId: Int? = null
    private lateinit var saveExpenseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_expense)

        textAmount = findViewById(R.id.editAmount)
        textDescription = findViewById(R.id.editDescription)
        textYear = findViewById(R.id.editYear)
        categoryDropdown = findViewById(R.id.dropdownCategory)
        imagePreview = findViewById(R.id.imagePreview)

        //Populate drop down box for months
        monthDropdown = findViewById(R.id.dropdownMonth)

        val months = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, months)
        monthDropdown.setAdapter(monthAdapter)

        //Force list drop down
        monthDropdown.setOnClickListener {
            monthDropdown.showDropDown()
        }

        //Get UserID and then fetching the corresponding categories for the drop down box
        val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("loggedInUserId", -1)

        if (userId == -1) {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        //Using UserID to fetch corresponding categorys
        val db = AppDatabase.getInstance(this)
        val expenseDAO = db.expenseDAO()
        expenseRepository = ExpenseRepository(expenseDAO)

        categoryDropdown = findViewById(R.id.dropdownCategory)

        lifecycleScope.launch {
            val expenses = expenseRepository.fetchExpenses(userId)


            val categoryNames = expenses.map { it.categoryId }.distinct()

            val categoryAdapter = ArrayAdapter(
                this@AddExpense,
                android.R.layout.simple_dropdown_item_1line,
                categoryNames
            )
            categoryDropdown.setAdapter(categoryAdapter)

            categoryDropdown.setOnClickListener {
                categoryDropdown.showDropDown()
            }
        }

        //Initilizing Button based on User logged in

        expenseRepository = ExpenseRepository(expenseDAO)
        saveExpenseButton = findViewById(R.id.btnSaveExpense)

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
                    findViewById<ImageView>(R.id.imagePreview).setImageURI(uri)
                }
            }
        }

        val uploadButton = findViewById<Button>(R.id.btnUploadImage)
        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            pickImageLauncher.launch(intent)
        }

        bottomNavBarView = findViewById(R.id.bottomNavBar)
        bottomNavBarView.selectedItemId = R.id.expense
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }

        bottomNavBarView.setOnItemSelectedListener{ item ->
            when (item.itemId){
                //This item will send the user to the Main Dashboard
                R.id.home ->{
                    startActivity(Intent(this, MainMenu::class.java))
                    true
                }
                //This item will send the user to the Adding Expense Dashboard
                R.id.expense -> {
                    startActivity(Intent(this, AddExpense::class.java))
                    true
                }
                //This item will send the user to the settings Dashboard
                R.id.menu -> {
                    startActivity(Intent(this, MenuDashboardActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Logic for save Expense Button

        saveExpenseButton.setOnClickListener {
            val amountText = textAmount.text.toString()
            val description = textDescription.text.toString()
            val month = monthDropdown.text.toString()
            val year = textYear.text.toString()
            val date = "$month $year"
            val categoryName = categoryDropdown.text.toString()

            // Basic validations
            if (amountText.isBlank() || description.isBlank() || month.isBlank() || year.isBlank() || categoryName.isBlank()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Need to fix this, it pulls the category ID from the drop down box and not the table,
            //Will fix in the morning, to tired now
            val categoryId = selectedCategoryId ?: 1 // TODO: Lookup based on category name

            if (userId == -1) {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expense = Expenses(
                userId = userId,
                categoryId = categoryId,
                expenseTitle = categoryName,
                expenseAmount = amount,
                dateAdded = date,
                description = description
            )

            // Save using repository
            lifecycleScope.launch {
                expenseRepository.addExpense(expense)
                Toast.makeText(this@AddExpense, "Expense saved successfully", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    }
}