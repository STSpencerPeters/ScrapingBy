package com.fake.scrapingby

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDAO {
    //SQL statement to create a new category
    @Insert
    suspend fun createCategory(categories: Categories)

    //SQL statement to search for all categories associated with the user.
    @Query("SELECT * FROM CATEGORY WHERE userId = :userId")
    suspend fun getCategoriesForUser(userId: Int): List<Categories>

    //SQL statement to get category based off the categoryName
    @Query("Select * from Category where categoryName = :categoryName and userId = :userId limit 1")
    suspend fun getCategoryByCategoryName(categoryName: String, userId: Int) : Categories?
}