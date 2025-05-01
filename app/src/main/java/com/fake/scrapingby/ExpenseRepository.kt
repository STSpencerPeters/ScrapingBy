package com.fake.scrapingby

class ExpenseRepository(private val expenseDAO: ExpensesDAO) {
    //Function to add an expense
    suspend fun addExpense(expenses: Expenses){
        expenseDAO.createExpense(expenses)
    }

    //Function to list expenses
    suspend fun fetchExpenses(userId: Int): List<Expenses>{
        return expenseDAO.showAllExpensesForUser(userId)
    }

    //Function to list fetch expenses in a category
    suspend fun fetchExpensesByCategory(userId: Int, categoryId: Int): List<Expenses> {
        return expenseDAO.getExpensesByCategory(userId, categoryId)
    }
}