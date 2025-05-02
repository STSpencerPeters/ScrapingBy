package com.fake.scrapingby

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BudgetDAO {
    //SQL Statement for creating minimum amount
    @Insert
    suspend fun createBudget(budget: Budget)

    //SQL Statement to delete Budget entry based on userId
    @Query("DELETE FROM Budget WHERE userId = :userId")
    suspend fun deleteBudgetByUser(userId: Int)

    //SQL Statement to fetch budget based on UserID
    @Query("SELECT * FROM Budget WHERE userId = :userId")
        suspend fun getBudget(userId: Int): Budget?
}