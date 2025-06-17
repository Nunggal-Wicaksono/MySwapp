package com.example.myswap.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SwapHistory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun swapHistoryDao(): SwapHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "swap_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
