package com.example.pwd_app.repository

import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.ImageData
import com.example.pwd_app.model.UpdateResponse
import retrofit2.Response


class DataRepository(
    private val apiInterface: ApiInterface
) {
    suspend fun uploadData(
        school_Name: String,
        po_office: String,
        image_name: String,
        image_type: String,
        image_pdf: String,
        upload_date: String,
        upload_time: String,
        EntryBy: String,
        Latitude: String,
        Longitude: String,
        user_upload_date: String,
        InspectionType: String,
        WorkorderNumber: String,
        Description: String,
        ags: String

    ): Response<UpdateResponse> {
        return apiInterface.uploadData(
            school_Name,
            po_office,
            image_name,
            image_type,
            image_pdf,
            upload_date,
            upload_time,
            EntryBy,
            Latitude,
            Longitude,
            user_upload_date,
            InspectionType,
            WorkorderNumber,
            Description,
            ags
        )
    }

}