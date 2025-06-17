package com.example.myswap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myswap.data.AppDatabase
import com.example.myswap.data.SwapHistory

class HomeViewModel(application: Application) : AndroidViewModel(application){
    private val db = AppDatabase.getDatabase(application)
    val history: LiveData<List<SwapHistory>> = db.swapHistoryDao().getAll()
}
