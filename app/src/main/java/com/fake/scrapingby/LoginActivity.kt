package com.fake.scrapingby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

//Class to check if the user is in the database and allow them into the system.
class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText : EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton : Button
    private lateinit var registerText : TextView

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameText)
        passwordEditText = findViewById(R.id.passwordText)
        loginButton = findViewById(R.id.loginButton)
        registerText = findViewById(R.id.registerText)

        val db = AppDatabase.getInstance(this)
        val userDao = db.userDAO()

        userRepository = UserRepository(db.userDAO())

        //Creating the logic for logging a user into the system and validating their information.
        loginButton.setOnClickListener{
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch{
                val user = userRepository.loginUser(username, password)
                if(user != null){
                    //Saving username for later use
                    /*
                        Code Attribution:
                        Shared Preferences in Android with Example, 2025.
                        This reference was able to help me store the username and ID so that it can be called to verify the user that is logged in.
                     */
                    val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
                    with(sharedPref.edit()){
                        putString("loggedInUsername", user.username)
                        putInt("loggedInUserId", user.id)
                        apply()
                    }
                    Log.d("LoginActivity", "User logged in: username=${user.username}, id=${user.id}")
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this@LoginActivity, MenuDashboardActivity::class.java))
                } else{
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Link for the user if they don't have an account.
        registerText.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }
}