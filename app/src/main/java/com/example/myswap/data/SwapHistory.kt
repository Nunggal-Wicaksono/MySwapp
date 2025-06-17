package com.example.myswap.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "swap_history")
data class SwapHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fromCurrency: String,
    val toCurrency: String,
    val fromAmount: Double,
    val toAmount: Double,
    val timestamp: Long = System.currentTimeMillis()
)
