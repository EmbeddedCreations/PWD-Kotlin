package com.example.pwd_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BuildingsTable")
data class SchoolBuildings(
    @PrimaryKey val id: String,
    val Division: String? = "",
    val unq_id: String? = "",
    val type_building: String? = "",
    val building_unq_id: String? = "",
    val same_structure_no: String? = "",
    val construction_year: String? = "",
    val nosStorey: String? = "",
    val built_up_area: String? = "",
    val foundation: String? = "",
    val roof_type: String? = "",
    val flooring_type: String? = "",
    val wall_type: String? = "",
    val door_type: String? = "",
    val window_type: String? = "",
    val water_supply: String? = "",
    val plumbing_type: String? = "",
    val sanitation_type: String? = "",
    val sanitation_block: String? = "",
    val internalpaint: String? = "",
    val externalpaint: String? = "",
    val electrification: String? = "",
    val IntElectrification: String? = "",
    val ExtElectrification: String? = "",
    val total: String? = "",
    val reg_date: String? = "",
    val regTime: String? = "",
    val EntryBy: String? = "",
    val EntryByPost: String? = ""
)