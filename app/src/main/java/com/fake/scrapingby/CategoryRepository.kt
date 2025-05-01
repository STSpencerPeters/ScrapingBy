package com.fake.scrapingby

import androidx.lifecycle.LiveData

class CategoryRepository(private val categoryDAO: CategoryDAO) {
    //Function to add a category
    suspend fun addCategory(categories: Categories){
        categoryDAO.createCategory(categories)
    }

    suspend fun getCategory(userId: Int): List<Categories>{
        return categoryDAO.getCategoriesForUser(userId)
    }
}