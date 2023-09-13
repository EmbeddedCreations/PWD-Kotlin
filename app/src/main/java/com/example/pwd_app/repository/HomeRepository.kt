package com.example.pwd_app.repository

import com.example.pwd_app.model.SchoolBuildings
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.RegisteredSchools
import com.example.pwd_app.model.WorkOrders
import com.example.pwd_app.network.NetworkUtil

class HomeRepository(
    private val apiInterface : ApiInterface,
    private val database : DatabaseHelper,
    private val applicationContext : Context
    ){

    private val schoolLiveData = MutableLiveData<List<RegisteredSchools>>()
    val schools : LiveData<List<RegisteredSchools>>
            get() = schoolLiveData

    private val buildingLiveData = MutableLiveData<List<SchoolBuildings>>()
    val buildings : LiveData<List<SchoolBuildings>>
        get() = buildingLiveData

    private val workOrderLiveData = MutableLiveData<List<WorkOrders>>()
    val workOrders : LiveData<List<WorkOrders>>
        get() = workOrderLiveData

    suspend fun getSchools(){

        if(NetworkUtil.isInternetAvailable(applicationContext) ){
            val result = apiInterface.getRegisteredSchools(Credentials.DEFAULT_ATC,Credentials.DEFAULT_PO)
            if(result.body()!=null){
                database.Dao().insertRegisteredSchools(result.body()!!)
                schoolLiveData.postValue(result.body())
            }
        }else{
            val registeredSchools = database.Dao().getAllRegisteredSchools()
            schoolLiveData.postValue(registeredSchools)
        }
    }

    suspend fun getBuildings(){

        if(NetworkUtil.isInternetAvailable(applicationContext)){
            val result = apiInterface.getSchoolBuildings()
            if(result.body() != null){
                database.Dao().insertSchoolBuildings(result.body()!!)
                buildingLiveData.postValue(result.body())
            }
        }else{
            val buildings = database.Dao().getAllSchoolBuildings()
            buildingLiveData.postValue(buildings)
        }
    }

    suspend fun getWorkOrders(){
        if(NetworkUtil.isInternetAvailable(applicationContext)){
            val result = apiInterface.getWorkOrders(Credentials.DEFAULT_ATC)
            if(result.body()!= null){
                database.Dao().insertWorkOrders(result.body()!!)
                workOrderLiveData.postValue(result.body())
            }
        }else{
            val workOrders = database.Dao().getAllWorkOrders()
            workOrderLiveData.postValue(workOrders)
        }
    }

}