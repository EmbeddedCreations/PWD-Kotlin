package com.example.pwd_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WorkOrderMaster")
data class WorkOrders(
    @PrimaryKey val SRN: String,
    val Division: String? = "",
    val Unq_ID: String? = "",
    val SchoolName: String? = "",
    val atc_office: String? = "",
    val latitude: String? = "",
    val longitude: String? = "",
    val ProjectOffice: String? = "",
    val SubDivision: String? = "",
    val WorkName: String? = "",
    val WorkType: String? = "",
    val AAnumber: String? = "",
    val AAdate: String? = "",
    val BaseAAPrice: String? = "",
    val TechApprovalPrice: String? = "",
    val TenderPrice: String? = "",
    val AcceptedRatePercent: String? = "",
    val AcceptedTenderValue: String? = "",
    val WorkorderNumber: String? = "",
    val WorkorderDate: String? = "",
    val AgreementNumber: String? = "",
    val TenderDate: String? = "",
    val DurationMonthsOrDays: String? = "",
    val EndDate: String? = "",
    val ReceivedAmt: String? = "",
    val SpentAmt: String? = "",
    val BalanceAmt: String? = "",
    val TotalExpence: String? = "",
    val ContractorName: String? = "",
    val ContractorContact: String? = "",
    val ContractorAddress: String? = "",
    val ScopeOfWork: String? = "",
    val CurrentWorkStatus: String? = "",
    val ExtensionAmt: String? = "",
    val Remarks: String? = "",
    val EntryBy: String? = "",
    val EntryPost: String? = "",
    val EntryDate: String? = "",
    val EntryTime: String? = "",
    val UpdateDate: String? = "",
    val UpdateTime: String? = ""
)
