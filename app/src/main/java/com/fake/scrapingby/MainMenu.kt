package com.fake.scrapingby

import android.content.Context
import android.content.Intent
import android.graphics.Color
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

    private lateinit var expense1: TextView
    private lateinit var expense2: TextView
    private lateinit var expense3: TextView


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

        //Binding the 3 Expense Outputs on the Expense Card
        expense1 = findViewById(R.id.expense1)
        expense2 = findViewById(R.id.expense2)
        expense3 = findViewById(R.id.expense3)


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

                    val allExpenses = expenseRepo.fetchExpenses(userId)
                    val totalSpent = allExpenses.sumOf { it.expenseAmount }

                    // Sort expenses descending
                    val sortedExpenses = allExpenses.sortedByDescending { it.expenseAmount }
                    val topTwo = sortedExpenses.take(2)
                    val remaining = sortedExpenses.drop(2)
                    val otherTotal = remaining.sumOf { it.expenseAmount }
                    val categoryRepo = CategoryRepository(db.categoryDAO())
                    val allCategories = categoryRepo.getCategory(userId) // returns List<Categories>
                    val categoryMap = allCategories.associateBy { it.id } // Map<Int, Categories>
                    val expense1Box = findViewById<TextView>(R.id.expense1)
                    val expense2Box = findViewById<TextView>(R.id.expense2)
                    val expense3Box = findViewById<TextView>(R.id.expense3)

                    // Set Top 1
                    if (topTwo.size > 0) {
                        val catName = categoryMap[topTwo[0].categoryId]?.categoryName ?: "Unknown"
                        expense1Box.text = "$catName: ${topTwo[0].description}\nR%.2f".format(topTwo[0].expenseAmount)
                        expense1Box.setBackgroundColor(Color.parseColor("#4267FF"))
                        expense1Box.visibility = View.VISIBLE
                    } else {
                        expense1Box.text = ""
                        expense1Box.setBackgroundColor(Color.WHITE)
                        expense1Box.visibility = View.GONE
                    }

// Set Top 2
                    if (topTwo.size > 1) {
                        val catName = categoryMap[topTwo[1].categoryId]?.categoryName ?: "Unknown"
                        expense2Box.text = "$catName: ${topTwo[1].description}\nR%.2f".format(topTwo[1].expenseAmount)
                        expense2Box.setBackgroundColor(Color.parseColor("#4267FF"))
                        expense2Box.visibility = View.VISIBLE
                    } else {
                        expense2Box.text = ""
                        expense2Box.setBackgroundColor(Color.WHITE)
                        expense2Box.visibility = View.GONE
                    }

                    // Set Other
                    if (remaining.isNotEmpty()) {
                        expense3Box.text = "Other\nR%.2f".format(otherTotal)
                        expense3Box.setBackgroundColor(Color.parseColor("#4267FF"))
                        expense3Box.visibility = View.VISIBLE
                    } else {
                        expense3Box.text = ""
                        expense3Box.setBackgroundColor(getColor(android.R.color.white))
                        expense3Box.visibility = View.GONE
                    }

                    // Budget Progress
                    val budget = withContext(Dispatchers.IO) {
                        db.budgetDAO().getBudget(userId)
                    }
                    val maxBudget = budget?.budgetMaximum ?: 10000.0

                    budgetProgressBar.max = maxBudget.toInt()
                    budgetProgressBar.progress = totalSpent.toInt()
                    textTotal.text = "Total: R${maxBudget.toInt()}"
                    textSpent.text = "Spent: R${totalSpent.toInt()}"
                }
            }
        } else {
            Log.d("Home Menu", "No User found, could not display name on Home Screen")
            textName.text = "Err"
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