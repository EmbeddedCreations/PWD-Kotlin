package com.example.pwd_app.repository

import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.ImageData
import com.example.pwd_app.model.UpdateResponse
import retrofit2.Response

class UploadRepository(
    private val apiInterface: ApiInterface
) {
    suspend fun editData(
        id: String,
        school_Name: String,
        po_office: String,
        image_name: String,
        EntryBy: String,
        Description: String,
    ): Response<UpdateResponse> {
        return apiInterface.EditData(
            id,
            school_Name,
            po_office,
            image_name,
            EntryBy,
            Description,
        )
    }
}