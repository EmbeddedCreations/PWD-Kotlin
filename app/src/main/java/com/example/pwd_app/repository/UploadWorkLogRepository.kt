package com.example.pwd_app.repository

import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.UpdateResponse
import retrofit2.Response

class UploadWorkLogRepository(
    private val apiInterface: ApiInterface
) {
    suspend fun updateLog(
        id : String,
        assignedJE : String,
        weekNumber : String,
        isWorkOnTime : String,
        isPhotoUploaded : String,
        sitePhysicalVisit : String,
        amountReleased : String,
        progressRating :String,
        weeklyProgressUpdateStatus : String
    ) : Response<UpdateResponse>{
        return apiInterface.uploadWorkLog(
            id,
            assignedJE,
            weekNumber,
            isWorkOnTime,
            isPhotoUploaded,
            sitePhysicalVisit,
            amountReleased,
            progressRating,
            weeklyProgressUpdateStatus
        )
    }
}