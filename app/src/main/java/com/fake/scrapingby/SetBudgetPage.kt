package com.fake.scrapingby

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SetBudgetPage : AppCompatActivity() {

    private lateinit var editMinBudget: EditText
    private lateinit var editMaxBudget: EditText
    private lateinit var btnSaveBudget: Button
    private lateinit var budgetRepository: BudgetRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_budget_page)

        editMinBudget = findViewById(R.id.editMinBudget)
        editMaxBudget = findViewById(R.id.editMaxBudget)
        btnSaveBudget = findViewById(R.id.btnSaveBudget)

        val db = AppDatabase.getInstance(this)
        val budgetDAO = db.budgetDAO()
        budgetRepository = BudgetRepository(budgetDAO)

        val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("loggedInUserId", -1)

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnSaveBudget.setOnClickListener {
            val min = editMinBudget.text.toString().toDoubleOrNull() ?: 0.0
            val max = editMaxBudget.text.toString().toDoubleOrNull()

            if (max == null || max <= 0.0) {
                Toast.makeText(this, "Please enter a valid max budget", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val budget = Budget(
                userId = userId,
                budgetMinimum = min,
                budgetMaximum = max
            )

            lifecycleScope.launch {
                // Save budget using repository
                budgetRepository.overwriteBudget(budget)
                Toast.makeText(this@SetBudgetPage, "Budget saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
