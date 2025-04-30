package com.fake.scrapingby

class ExpenseRepository(private val expenseDAO: ExpensesDAO) {
    //Function to add an expense
    suspend fun addExpense(expenses: Expenses){
        expenseDAO.createExpense(expenses)
    }
}