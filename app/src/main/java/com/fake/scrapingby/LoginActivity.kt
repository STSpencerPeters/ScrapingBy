package com.fake.scrapingby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText : EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton : Button
    private lateinit var registerText : TextView

    private lateinit var userRepository: Repository

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

        userRepository = Repository(db.userDAO())

        loginButton.setOnClickListener{
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch{
                val user = userRepository.loginUser(username, password)
                if(user != null){
                    //Saving username for later use
                    val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
                    with(sharedPref.edit()){
                        putString("loggedInUsername", username)
                        apply()
                    }
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()//Change when dashboard is added
                } else{
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerText.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }
}