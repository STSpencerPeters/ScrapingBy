package com.fake.scrapingby

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDAO {
    //SQL function to register the User
    @Insert
    suspend fun registerUser(user: User)

    //SQL function to log the user in if they have the correct details
    @Query("SELECT * FROM Users WHERE username = :username AND password = :password")
    suspend fun loginUser(username: String, password: String): User?

    //SQL function to get a User based off of their Username
    @Query("SELECT * FROM Users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    //SQL function to get a User based off of their ID
    @Query("SELECT * FROM Users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    //SQL function to update the Users information
    @Update
    suspend fun updateUser(user: User)

    //SQL function to update the Users Password
    @Query("UPDATE Users SET password =:password WHERE id = :userId")
    suspend fun updateUserPassword(password: String, userId: Int)
}