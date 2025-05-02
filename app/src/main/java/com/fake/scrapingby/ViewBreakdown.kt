package com.fake.scrapingby

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ViewBreakdown : AppCompatActivity() {

    private lateinit var bottomNavBarView: BottomNavigationView
    private lateinit var monthDropdown: AutoCompleteTextView
    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var expenseRepository: ExpenseRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var adapter: ExpenseAdapter
    private var allExpenses: List<Expenses> = emptyList()
    private lateinit var textMonthlyTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_breakdown)

        bottomNavBarView = findViewById(R.id.bottomNavBar)
        bottomNavBarView.menu.findItem(R.id.expense).isChecked = false

        //For Total Amount at top of Screen
        textMonthlyTotal = findViewById(R.id.textMonthlyTotal)

        //Initialize monthly drop down
        monthDropdown = findViewById(R.id.dropdownMonthSelector)
        expenseRecyclerView = findViewById(R.id.recyclerExpenses)

        val db = AppDatabase.getInstance(this)
        expenseRepository = ExpenseRepository(db.expenseDAO())
        categoryRepository = CategoryRepository(db.categoryDAO())

        adapter = ExpenseAdapter(this@ViewBreakdown, emptyList())
        expenseRecyclerView.adapter = adapter

        val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("loggedInUserId", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                allExpenses = expenseRepository.fetchExpenses(userId)

                val months = allExpenses.map { it.dateAdded }.distinct().sorted()
                val monthAdapter = ArrayAdapter(
                    this@ViewBreakdown,
                    android.R.layout.simple_dropdown_item_1line,
                    months
                )
                monthDropdown.setAdapter(monthAdapter)

                monthDropdown.setOnClickListener {
                    monthDropdown.showDropDown()
                }

                monthDropdown.setOnItemClickListener { _, _, position, _ ->
                    val selectedMonth = monthAdapter.getItem(position)
                    val filtered = allExpenses.filter { it.dateAdded == selectedMonth }

                    val monthlyTotal = filtered.sumOf { it.expenseAmount }
                    textMonthlyTotal.text = "Total Spent: R%.2f".format(monthlyTotal)

                    adapter.updateList(filtered)
                }

            }
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
