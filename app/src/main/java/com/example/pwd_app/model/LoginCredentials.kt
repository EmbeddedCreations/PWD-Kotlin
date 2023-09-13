package com.example.pwd_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LoginCredTable")
data class LoginCredentials(
    @PrimaryKey val id: String,
    val username: String,
    val po_office: String,
    val atc_office: String,
    val password :  String,
    val position: String,
    val ErStatus: String
)
