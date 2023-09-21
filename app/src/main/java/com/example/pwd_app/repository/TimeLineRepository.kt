package com.example.pwd_app.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.WorkOrderTimelineModel
import com.example.pwd_app.network.NetworkUtil

class TimeLineRepository(
    private val apiInterface: ApiInterface,
    private val database : DatabaseHelper,
    private val applicationContext : Context
) {
    private val timelineLiveData = MutableLiveData<List<WorkOrderTimelineModel>>()

    val timeLine : LiveData<List<WorkOrderTimelineModel>>
        get() = timelineLiveData

    suspend fun getTimeline(po_office :String){
        if(NetworkUtil.isInternetAvailable(applicationContext)){
            val result = apiInterface.getWorkOrderTimeline(po_office)
            if(result.body() != null){
                database.Dao().insertTimeLine(result.body()!!)
                timelineLiveData.postValue(result.body())
            }
        }else{
            val timeLines = database.Dao().getTimelines(po_office)
            timelineLiveData.postValue(timeLines)
        }
    }
}