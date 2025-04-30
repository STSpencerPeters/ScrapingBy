package com.fake.scrapingby

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpensesDAO {
    //SQL statement to create new expenses
    @Insert
    suspend fun createExpense(expenses: Expenses)

    //SQL statement search for expenses associated with the user
    @Query("Select * From Expenses where userId = :userId")
    suspend fun showAllExpensesForUser(userId: Int): List<Expenses>

    //SQL statement to delete expense entries.
    @Delete
    suspend fun deleteExpense(expenses: Expenses)
}