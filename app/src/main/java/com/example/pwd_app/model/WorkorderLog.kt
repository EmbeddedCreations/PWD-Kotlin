package com.example.pwd_app.model

data class WorkorderLog(
    val ID: String,
    val WorkOrderSRN: String?= "",
    val SchID: String? ="",
    val schDivision: String?= "",
    val poOffice: String? = "",
    val atcOffice: String? = "",
    val workOrderName: String?= "",
    val isWorkOnTime: String?= "",
    val isPhotoUploaded: String?= "",
    val sitePhysicalVisit: String?= "",
    val amountReleased: String?= "",
    val progressRating: String?= "",
    val scheduledDate: String? = "",
    val weekStartDate: String?= "",
    val weekEndDate: String?= "",
    val weekNumber: String?= "",
    val inspectionYear: String?= "",
    val weeklyProgressUpdateFlag:  String?= "",
    val weeklyProgressUpdateStatus: String?= "",
    val contractorName: String?= "",
    val assignedJE: String? = "",
    val updatedDate: String? ="",
    val updatedTime: String?= ""
)
