package com.fake.scrapingby

class GetExpenseByCategoryUseCase (
    private val categoryRepository: CategoryRepository,
    private val expenseRepository: ExpenseRepository
    ) {

    suspend operator fun invoke(userId: Int, categoryName: String): List<Expenses> {
        val category = categoryRepository.getCategoryByName(categoryName, userId)
        return if (category != null) {
            expenseRepository.fetchExpensesByCategory(userId, category.id)
        } else {
            emptyList() // Or throw an exception if preferred
        }
    }
}