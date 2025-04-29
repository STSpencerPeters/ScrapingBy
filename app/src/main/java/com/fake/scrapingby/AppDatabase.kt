package com.fake.scrapingby

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Users ADD COLUMN firstName TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE Users ADD COLUMN surname TEXT NOT NULL DEFAULT ''")
    }
}


@Database(entities = [User::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDAO() : UserDAO

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
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}