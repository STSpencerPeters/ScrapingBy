package com.fake.scrapingby

import androidx.room.Index
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/*
    Code Attribution:
    Room Database - Using Foreign Keys!, 2023.
    This reference helped to create foreign keys to link my tables
 */
//Created table for Budget with its fields.
@Entity(
    tableName = "Budget",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId")]
)
data class Budget(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val userId: Int,
    val budgetMinimum: Double,
    val budgetMaximum: Double
)