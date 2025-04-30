package com.fake.scrapingby

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpensesDAO {
    @Insert
    suspend fun createExpense(expenses: Expenses)

    @Query("Select * From Expenses where userId = :userId")
    suspend fun showAllExpensesForUser(userId: Int): List<Expenses>

    @Delete
    suspend fun deleteExpense(expenses: Expenses)
}