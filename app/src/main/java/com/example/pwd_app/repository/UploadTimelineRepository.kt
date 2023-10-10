package com.example.pwd_app.repository

import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.UpdateResponse
import com.example.pwd_app.model.UploadTimelineModel
import retrofit2.Response
import retrofit2.http.Body

class UploadTimelineRepository (
    private val apiInterface: ApiInterface
){
    suspend fun setTimeline(@Body workOrderData: UploadTimelineModel?): Response<UpdateResponse> {
        return apiInterface.updateWorkOrder(workOrderData)
    }
}