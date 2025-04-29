package com.fake.scrapingby

class Repository(private val userDAO: UserDAO) {

    suspend fun registerUSer(user : User){
        userDAO.registerUser(user)
    }

    suspend fun loginUser(username: String, password: String) : User?{
        return userDAO.loginUser(username, password)
    }

    suspend fun isUsernameTaken(username: String): Boolean{
        return userDAO.getUserByUsername(username) != null
    }
}