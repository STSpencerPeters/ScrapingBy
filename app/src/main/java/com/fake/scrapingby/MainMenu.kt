package com.fake.scrapingby

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainMenu : AppCompatActivity() {


    private lateinit var bottomNavBarView:BottomNavigationView
    private lateinit var textName: TextView
    private lateinit var buttonBreakdown: Button
    private lateinit var budgetProgressBar: ProgressBar
    private lateinit var textTotal: TextView
    private lateinit var textSpent: TextView

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
        bottomNavBarView.setOnItemSelectedListener{ item ->
            when (item.itemId){
                //This item will send the user to the Main Dashboard
                R.id.home ->{
                    startActivity(Intent(this, MainMenu::class.java))
                    true
                }
                //This item will send the user to the Adding Expense Dashboard
                R.id.expense -> {
                    //startActivity(Intent(this, ExpenseActivity::clas.java))
                    true
                }
                //This item will send the user to the settings Dashboard
                R.id.menu -> {
                    //startActivity(Intent(this, MenuDashboardActivity::class.java))
                    true
                }
                else -> false
            }
        }

        budgetProgressBar.setOnClickListener{
            //startActivity(Intent(this, BudgetActivity::class.java))
        }
    }
}