package com.fake.scrapingby

class Repository(private val userDAO: UserDAO, private val categoryDAO: CategoryDAO, private val expenseDAO: ExpensesDAO) {

    //Function to register the User
    suspend fun registerUSer(user : User){
        userDAO.registerUser(user)
    }

    //Function to Log the User into the system.
    suspend fun loginUser(username: String, password: String) : User?{
        return userDAO.loginUser(username, password)
    }

    //Function to check if the Username of the User registering is available.
    suspend fun isUsernameTaken(username: String): Boolean{
        return userDAO.getUserByUsername(username) != null
    }

    //Function to add a category
    suspend fun addCategory(categories: Categories){
        categoryDAO.createCategory(categories)
    }

    //Function to add an expense
    suspend fun addExpense(expenses: Expenses){
        expenseDAO.createExpense(expenses)
    }
}