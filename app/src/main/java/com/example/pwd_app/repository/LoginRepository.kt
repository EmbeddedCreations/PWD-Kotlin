package com.example.pwd_app.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.LoginCredentials
import com.example.pwd_app.network.NetworkUtil

class LoginRepository(
    private val apiInterface: ApiInterface,
    private val database: DatabaseHelper,
    private val applicationContext: Context
) {

    private val loginLiveData = MutableLiveData<List<LoginCredentials>>()
    val users: LiveData<List<LoginCredentials>>
        get() = loginLiveData

    suspend fun getUsers() {
        if (NetworkUtil.isInternetAvailable(applicationContext)) {
            val result = apiInterface.getUserCredentials()
            if (result.body() != null) {
                database.Dao().insertLoginCredentials(result.body()!!)
                loginLiveData.postValue(result.body())
            }
        } else {
            val userCredentials = database.Dao().getAll()
            loginLiveData.postValue(userCredentials)
        }
    }

}