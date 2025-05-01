package com.fake.scrapingby

import android.app.Activity
import android.content.Intent
import android.icu.text.DecimalFormat
import android.icu.util.CurrencyAmount
import android.net.Uri
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text

class AddExpense : AppCompatActivity() {

    private lateinit var bottomNavBarView: BottomNavigationView
    private lateinit var textDescription: TextView
    private lateinit var selectedMonth: String
    private lateinit var selectedYear: String
    private lateinit var selectedCategory: Categories
    private lateinit var uploadImageButton: Button
    private lateinit var saveExpenseButton: Button
    private lateinit var monthDropdown: AutoCompleteTextView
    private lateinit var yearDropdown: AutoCompleteTextView
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_expense)

        val amountField = findViewById<EditText>(R.id.editAmount)


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
    }
}