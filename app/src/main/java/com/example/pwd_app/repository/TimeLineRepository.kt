package com.example.pwd_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.WorkOrderTimelineModel

class TimeLineRepository(
    private val apiInterface: ApiInterface
) {
    private val timelineLiveData = MutableLiveData<WorkOrderTimelineModel>()

    val timeLine : LiveData<WorkOrderTimelineModel>
        get() = timelineLiveData

    suspend fun getTimeline(po_office :String,workorder_no: String){

    }
}