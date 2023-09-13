package com.example.pwd_app.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiUtility {
    val BASE_URL = "https://embeddedcreation.in/tribalpwd/adminPanelNewVer2/"

    fun getInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}