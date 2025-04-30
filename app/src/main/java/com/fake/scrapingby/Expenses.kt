package com.fake.scrapingby

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Expenses",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId")]
)

data class Expenses(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val expenseTitle : String,
    val expenseAmount : Double,
    val dateAdded : String,
    val description: String
)