package com.example.pwd_app.repository

import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.UpdateResponse
import retrofit2.Response

class LoginLogRepository(
    private val apiInterface: ApiInterface
) {
    suspend fun addLog(
        po_office : String,
        EntryBy : String
    ): Response<UpdateResponse> {
        return apiInterface.loginLog(
            po_office,
            EntryBy
        )
    }
}