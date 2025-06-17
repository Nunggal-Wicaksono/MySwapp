package com.example.myswap.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SwapHistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: SwapHistory)

    @Query("SELECT * FROM swap_history ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<SwapHistory>>
}
