package com.fake.scrapingby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

//Cass that handles the screen that holds the buttons for, Profile, Preferences, Categories and Logout
class MenuDashboardActivity : AppCompatActivity() {


    private lateinit var bottomNavBarView: BottomNavigationView
    private lateinit var profileCardLayout : LinearLayout
    private lateinit var preferencesCardlayout: LinearLayout
    private lateinit var categoryCardLayout: LinearLayout
    private lateinit var logoutCardLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_dashboard)

        bottomNavBarView = findViewById(R.id.bottomNavigation)
        bottomNavBarView.selectedItemId = R.id.menu
        profileCardLayout = findViewById(R.id.profileCard)
        preferencesCardlayout = findViewById(R.id.preferencesCard)
        categoryCardLayout = findViewById(R.id.categoryCard)
        logoutCardLayout = findViewById(R.id.logoutCard)

        bottomNavBarView.selectedItemId = R.id.menu

        //Card to send the user to the Profile Page
        profileCardLayout.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        /*

            //Card to send the user to the Preferences Page
            preferencesCardlayout.setOnClickListener{
                startActivity(Intent(this, PreferencesActivity::class.java))
            }

         */

            //Card to send the user to the Category Page
            categoryCardLayout.setOnClickListener{
                startActivity(Intent(this, CategoryActivity::class.java))
            }


        //Card to log the user out of the system and send them back to the Login Page.
        logoutCardLayout.setOnClickListener{
            //Clearing sharedPreferences so user is logged out.
            val sharedPrefences = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
            with(sharedPrefences.edit()){
                clear()
                apply()
            }

            val intent = Intent(this, LoginActivity::class.java)
            /*
                Code Attribution:
                How to prevent going back to the previous activity?, 2011.
                This stackoverflow page was talking about how to stop users from going back to the previous screen. The user Pulak provided an answer that worked for my situation.
             */
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        //Navigation bar locations
        bottomNavBarView.setOnItemSelectedListener{ item ->
            when (item.itemId){
                //This item will send the user to the Main Dashboard
                R.id.home ->{
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                //This item will send the user to the Adding Expense Dashboard
                R.id.expense -> {
                    //startActivity(Intent(this, ExpenseActivity::clas.java))
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