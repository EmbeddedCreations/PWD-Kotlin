package com.example.pwd_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.model.ImageData
import com.example.pwd_app.model.LoginCredentials

class LocalDatabaseRepository(
    private val database: DatabaseHelper
) {
    private val imageData = MutableLiveData<List<ImageData>>()
    val images : LiveData<List<ImageData>>
        get() = imageData
    private val imageCount = MutableLiveData<Int>()
    val dbCount : LiveData<Int>
        get() = imageCount

    suspend fun getCount(){
        imageCount.postValue(database.Dao().getDbCount())
    }
    suspend fun getData(){
        imageData.postValue(database.Dao().getAllImages())
    }

}