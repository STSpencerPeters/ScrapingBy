package com.fake.scrapingby

import androidx.lifecycle.LiveData

class CategoryRepository(private val categoryDAO: CategoryDAO) {
    //Function to add a category
    suspend fun addCategory(categories: Categories){
        categoryDAO.createCategory(categories)
    }

    //Function to get all the categories associated with the userId
    suspend fun getCategory(userId: Int): List<Categories>{
        return categoryDAO.getCategoriesForUser(userId)
    }

    //Function to check if the Category name has been used before
    suspend fun isCategoryNameTaken(categoryName: String, userId: Int) : Boolean{
        return categoryDAO.getCategoryByCategoryName(categoryName, userId) != null
    }

    //Function to get Categories by name
    suspend fun getCategoryByName(categoryName: String, userId: Int): Categories? {
        return categoryDAO.getCategoryByCategoryName(categoryName, userId)
    }
}