package com.fake.scrapingby

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

//User table
@Entity(tableName = "Users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username : String,
    val password : String,
    val profileImage : String? = null, //So that the user can add Image later.
    val firstName : String,
    val surname : String
)