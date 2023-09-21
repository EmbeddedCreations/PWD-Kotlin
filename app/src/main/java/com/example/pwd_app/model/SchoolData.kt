package com.example.pwd_app.model

import androidx.room.PrimaryKey

data class SchoolData(
    @PrimaryKey val id: String? = "",
    val school_name: String? = "",
    val po_office: String? = "",
    val image_name: String? = "",
    val image_type: String? = "",
    val image_pdf: String? = "",
    val upload_date: String? = "",
    val upload_time: String? = "",
    val EntryBy: String? = "",
    val Latitude: String? = "",
    val Longitude: String? = "",
    val user_upload_date: String? = "",
    val InspectionType: String? = "",
    val WorkorderNumber: String? = "",
    val Description: String? = "",
    val ags: String? = ""
)
