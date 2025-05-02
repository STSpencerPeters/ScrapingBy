package com.fake.scrapingby

class BudgetRepository(private val budgetDAO: BudgetDAO) {
    //Function to add Budget Minimum
    suspend fun overwriteBudget(budget: Budget) {
        budgetDAO.deleteBudgetByUser(budget.userId)
        budgetDAO.createBudget(budget)
    }

    //Function to get a user budget based on their UserId
    suspend fun getBudget(userId: Int): Budget? {
        return budgetDAO.getBudget(userId)
    }
}