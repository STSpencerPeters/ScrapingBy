package com.fake.scrapingby

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
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var surnameEditText : EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginText : TextView

    private lateinit var userRepository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        firstNameEditText = findViewById(R.id.firstNameText)
        surnameEditText = findViewById(R.id.surnameText)
        usernameEditText = findViewById(R.id.usernameText)
        passwordEditText = findViewById(R.id.passwordText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordText)
        registerButton = findViewById(R.id.registerButton)
        loginText = findViewById(R.id.loginText)


        val db = AppDatabase.getInstance(this)
        val userDao = db.userDAO()
        userRepository = Repository(db.userDAO())

        //Creating the logic for registering a User and storing it to the database. Making sure all the information is accounted for.
        registerButton.setOnClickListener{
            val firstName = firstNameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            lifecycleScope.launch{
                if(firstName.isBlank() || surname.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()){
                    Toast.makeText(this@RegisterActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if(password != confirmPassword){
                    Toast.makeText(this@RegisterActivity, "Please Make Sure Passwords Match", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if(userRepository.isUsernameTaken(username)){
                    Toast.makeText(this@RegisterActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = User(firstName = firstName, surname = surname, username = username, password = password)
                    userRepository.registerUSer(newUser)
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        loginText.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}