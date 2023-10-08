package com.example.pwd_app.model

import com.google.gson.annotations.SerializedName

data class EditModel(
    @SerializedName("school_name")
    val schoolName: String,
    @SerializedName("po_office")
    val poOffice: String,
    @SerializedName("image_name")
    val imageName: String,
    @SerializedName("EntryBy")
    val EntryBy: String,
    @SerializedName("Description")
    val description: String,
){

}
