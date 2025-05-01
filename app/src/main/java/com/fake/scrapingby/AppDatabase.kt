package com.fake.scrapingby

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase





//Creating the database to store the information.
@Database(entities = [User::class, Expenses::class, Categories::class, Budget::class], version = 4)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDAO() : UserDAO
    abstract fun expenseDAO() : ExpensesDAO
    abstract fun categoryDAO(): CategoryDAO
    abstract fun budgetDAO() : BudgetDAO

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                )
                    //.fallbackToDestructiveMigration()
                    //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}