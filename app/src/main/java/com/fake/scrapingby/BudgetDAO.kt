package com.fake.scrapingby

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BudgetDAO {
    //SQL Statement for creating minimum amount
    @Insert
    suspend fun createMinimumAmmount(budget: Budget)

    //SQL Statement for creating maximum amount
    @Insert
    suspend fun createMaximumAmount(budget: Budget)

}