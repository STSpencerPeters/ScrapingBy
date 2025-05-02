package com.fake.scrapingby

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/*
    Code Attribution:
    Room Database - Using Foreign Keys!, 2023.
    This reference helped to create foreign keys to link my tables
 */
//Created table for Expense with its fields.
@Entity(
    tableName = "Expenses",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    ),
                  ForeignKey(
                      entity = Categories::class,
                      parentColumns = ["id"],
                      childColumns = ["categoryId"],
                      onDelete = ForeignKey.CASCADE
                  )],
    indices = [Index("userId"), Index("categoryId")]
)

data class Expenses(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val categoryId: Int? = null,
    val expenseTitle : String,
    val expenseAmount : Double,
    val dateAdded : String,
    val description: String,
    val expenseImage: String? = null
)