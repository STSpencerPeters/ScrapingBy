package com.fake.scrapingby

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/*
    Code Attribution:
    Room Database - Using Foreign Keys!, 2023.
    This reference helped to create foreign keys to link my tables
 */
//Created table for Category with its fields.
@Entity(
    tableName = "Category",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId")]
)
data class Categories(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryName: String,
    val categoryImage: String,
    val userId: Int
)