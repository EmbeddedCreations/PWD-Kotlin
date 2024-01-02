package com.example.pwd_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LoginTableCred")
data class LoginCredentials(
    @PrimaryKey
    val EID: String,
    val LgnErName: String,
    val ErPosition: String,
    val AssignedPoOffice: String,
    val cnfLgnPass: String,
    val status: String,
)
