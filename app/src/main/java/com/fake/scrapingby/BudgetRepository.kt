package com.fake.scrapingby

class BudgetRepository(private val budgetDAO: BudgetDAO) {
    //Function to add Budget Minimum
    suspend fun createMinumumAmount(budget: Budget){
        budgetDAO.createMinimumAmmount(budget)
    }

    //Function to add Budget Maximum
    suspend fun createMaximumAmount(budget: Budget){
        budgetDAO.createMaximumAmount(budget)
    }
}