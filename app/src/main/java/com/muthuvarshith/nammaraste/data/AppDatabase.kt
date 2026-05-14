package com.muthuvarshith.nammaraste.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muthuvarshith.nammaraste.model.Report
import com.muthuvarshith.nammaraste.model.User

// Incremented version to 2 to handle schema changes
@Database(entities = [User::class, Report::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_raste_db"
                )
                .fallbackToDestructiveMigration() // Automatically clears old data on schema change
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
