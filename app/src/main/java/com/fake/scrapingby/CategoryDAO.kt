package com.fake.scrapingby

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDAO {
    @Insert
    suspend fun createCategory(categories: Categories)

    @Query("SELECT * FROM CATEGORY WHERE userId = :userId")
    suspend fun getCategoriesForUser(userId: Int): List<Categories>
}