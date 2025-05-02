package com.fake.scrapingby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainMenu : AppCompatActivity() {

    private lateinit var userRepo: UserRepository

    private lateinit var bottomNavBarView:BottomNavigationView
    private lateinit var textName: TextView
    private lateinit var buttonBreakdown: Button
    private lateinit var budgetProgressBar: ProgressBar
    private lateinit var textTotal: TextView
    private lateinit var textSpent: TextView

    private var currentUser:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)

        textName = findViewById(R.id.txtName)
        buttonBreakdown = findViewById(R.id.btnViewBreakdown)
        budgetProgressBar = findViewById(R.id.progressBar)
        textTotal = findViewById(R.id.txtTotal)
        textSpent = findViewById(R.id.txtSpentAmt)
        bottomNavBarView = findViewById(R.id.bottomNavBar)

        val db = AppDatabase.getInstance(this)
        val userDAO = db.userDAO()
        userRepo = UserRepository(db.userDAO())

        val budgetDAO = db.budgetDAO()
        val budgetRepo = BudgetRepository(budgetDAO)

        val expenseRepo = ExpenseRepository(db.expenseDAO())

        val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("loggedInUserId", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                val user = userRepo.getUserById(userId)
                currentUser = user
                user?.let {
                    textName.text = it.firstName

                    //Fetch all expenses
                    val allExpenses = expenseRepo.fetchExpenses(userId)
                    val totalSpent = allExpenses.sumOf { it.expenseAmount }

                    //Fetch Budget based on user ID
                    val budget = withContext(Dispatchers.IO) {
                        db.budgetDAO().getBudget(userId)
                    }

                    //Gets the max amount for the progress bar on the main page
                    val maxBudget = budget?.budgetMaximum ?: 10000.0  // fallback default

                    budgetProgressBar.max = maxBudget.toInt()
                    budgetProgressBar.progress = totalSpent.toInt()

                    textTotal.text = "Total: R${maxBudget.toInt()}"
                    textSpent.text = "Spent: R${totalSpent.toInt()}"
                }
            }
        } else
        {
            Log.d("Home Menu", "No User found, could not display name on Home Screen")
            textName.setText("Err")
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

        //Clicking progress bar navigates to the SetBudgetPage
        budgetProgressBar.setOnClickListener {
            startActivity(Intent(this, SetBudgetPage::class.java))
        }


        buttonBreakdown.setOnClickListener{
            startActivity(Intent(this, ViewBreakdown::class.java))
        }
    }
    override fun onResume() {
        super.onResume()
        refreshBudgetData()
    }

    private fun refreshBudgetData() {
        val db = AppDatabase.getInstance(this)
        val userDAO = db.userDAO()
        val budgetDAO = db.budgetDAO()
        val budgetRepo = BudgetRepository(budgetDAO)
        val expenseRepo = ExpenseRepository(db.expenseDAO())

        val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("loggedInUserId", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                val user = userRepo.getUserById(userId)
                currentUser = user
                user?.let {
                    textName.text = it.firstName

                    val allExpenses = expenseRepo.fetchExpenses(userId)
                    val totalSpent = allExpenses.sumOf { it.expenseAmount }

                    val budget = budgetRepo.getBudget(userId)
                    val maxBudget = budget?.budgetMaximum ?: 10000.0

                    budgetProgressBar.max = maxBudget.toInt()
                    budgetProgressBar.progress = totalSpent.toInt()

                    textTotal.text = "Total: R${maxBudget.toInt()}"
                    textSpent.text = "Spent: R${totalSpent.toInt()}"
                }
            }
        }
    }
}