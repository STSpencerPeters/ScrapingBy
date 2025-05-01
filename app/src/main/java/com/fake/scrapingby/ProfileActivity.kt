package com.fake.scrapingby

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class ProfileActivity : AppCompatActivity() {

    private lateinit var userRepo: UserRepository

    private lateinit var firstNameText: TextView
    private lateinit var surnameText: TextView
    private lateinit var usernameText: TextView
    private lateinit var profilePicture: ImageView
    private lateinit var changePic: TextView
    private lateinit var backButtons: ImageButton

    private var currentUser: User? = null

    //Variable to pick Images
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        val db = AppDatabase.getInstance(this)
        val userDao = db.userDAO()
        userRepo = UserRepository(userDao)

        firstNameText = findViewById(R.id.firstNameValue)
        surnameText = findViewById(R.id.surnameValue)
        usernameText = findViewById(R.id.usernameValue)
        profilePicture = findViewById(R.id.profileImage)
        changePic = findViewById(R.id.changePictureText)
        backButtons = findViewById(R.id.backButton)

        // Register ActivityResultLauncher
        /*
            Code Attribution:
            android pick images from gallery (now startActivityForResult is depreciated), 2021.
            This reference helped me create an image picker, to select an Image for my profile Picture.
         */
        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let { uri ->
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    profilePicture.setImageURI(uri)

                    // Save updated URI to DB
                    lifecycleScope.launch {
                        currentUser?.let { user ->
                            val updatedUser = user.copy(profileImage = uri.toString())
                            userRepo.updateUser(updatedUser)
                            currentUser = updatedUser
                        }
                    }
                }
            }
        }

        /*
            Code Attribution:
            Shared Preferences in Android with Example, 2025.
            This reference was able to help me fetch the username and userId that was previously stored and use it to verify the logged in user
            This is used multiple more times throughout the project.
        */
        val sharedPref = getSharedPreferences("Usersession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("loggedInUserId", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                val user = userRepo.getUserById(userId)
                currentUser = user
                user?.let {
                    firstNameText.text = it.firstName
                    surnameText.text = it.surname
                    usernameText.text = it.username

                    it.profileImage?.let { uriString ->
                        val uri = Uri.parse(uriString)
                        profilePicture.setImageURI(uri)
                    }
                }
            }
        } else {
            Toast.makeText(this, "No logged-in user found", Toast.LENGTH_SHORT).show()
        }

        backButtons.setOnClickListener {
            startActivity(Intent(this, MenuDashboardActivity::class.java))
        }

        changePic.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            pickImageLauncher.launch(intent)
        }
    }
}
