package com.fake.scrapingby

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDatabase.getInstance(this)
        val userDao = db.userDAO()

        lifecycleScope.launch {
            // Register a new user
            //val newUser = User(username = "TestUser", password = "1234")
            //userDao.registerUser(newUser)

            // Get the latest user (id = 1 if this is first)
            val testUser = userDao.getUserById(2)

            if (testUser != null) {
                Log.d("RoomTest", "User: ${testUser.username}, Pass: ${testUser.password}")
                if (testUser.profileImage == null) {
                    Log.d("RoomTest", "No profile image yet.")
                }
            } else {
                Log.d("RoomTest", "No user found.")
            }

            // Check if login is successful
            val loginResult = userDao.loginUser("TestUsers", "1234")

            if (loginResult != null) {
                Log.d("LoginTest", "Login successful: ${loginResult.username}")
            } else {
                Log.d("LoginTest", "Login failed: incorrect credentials")
            }
        }
    }
}