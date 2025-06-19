package com.example.myswap.data

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, SwapHistory::class /* dll */], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun swapHistoryDao(): SwapHistoryDao
    abstract fun userDao(): UserDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "swap_db"
                ).fallbackToDestructiveMigration() // ✅ tambahkan ini
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
