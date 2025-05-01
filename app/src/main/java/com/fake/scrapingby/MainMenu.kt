package com.fake.scrapingby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

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

        val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("loggedInUserId", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                val user = userRepo.getUserById(userId)
                currentUser = user
                user?.let {
                    textName.text = it.firstName
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
                    //startActivity(Intent(this, ExpenseActivity::class.java))
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




        budgetProgressBar.setOnClickListener{
            //startActivity(Intent(this, BudgetActivity::class.java))
        }


        buttonBreakdown.setOnClickListener{
            //startActivity(Intent(this, BudgetBreakdownActivity::class.java))
        }
    }
}