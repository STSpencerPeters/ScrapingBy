package com.fake.scrapingby

class CategoryRepository(private val categoryDAO: CategoryDAO) {
    //Function to add a category
    suspend fun addCategory(categories: Categories){
        categoryDAO.createCategory(categories)
    }
}