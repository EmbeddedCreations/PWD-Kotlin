package com.example.pwd_app.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.RegisteredSchools
import com.example.pwd_app.network.NetworkUtil

class MapRepository(
    private val apiInterface: ApiInterface,
    private val database: DatabaseHelper,
    private val applicationContext: Context
) {

    private val schoolLiveData = MutableLiveData<List<RegisteredSchools>>()
    val schools: LiveData<List<RegisteredSchools>>
        get() = schoolLiveData

    suspend fun getSchools() {

        if (NetworkUtil.isInternetAvailable(applicationContext)) {

            val result =
                apiInterface.getSchoolCoordinates()
            if (result.body() != null) {
                database.Dao().insertRegisteredSchools(result.body()!!)
                schoolLiveData.postValue(result.body())
            }
        } else {
            val registeredSchools = database.Dao().getAllRegisteredSchools()
            schoolLiveData.postValue(registeredSchools)
        }
    }
}