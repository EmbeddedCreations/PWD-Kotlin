package com.example.pwd_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pwd_app.model.ImageData
import com.example.pwd_app.model.LoginCredentials
import com.example.pwd_app.model.RegisteredSchools
import com.example.pwd_app.model.WorkOrderTimelineModel
import com.example.pwd_app.model.SchoolBuildings
import com.example.pwd_app.model.WorkOrders

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = LoginCredentials::class)
    suspend fun insertLoginCredentials(credentials: List<LoginCredentials>)

    // Select all login credentials
    @Query("SELECT * FROM LoginCredTable")
    suspend fun getAll(): List<LoginCredentials>

    //RegisteredSchoolsTable
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = RegisteredSchools::class)
    suspend fun insertRegisteredSchools(schools: List<RegisteredSchools>)

    @Query("SELECT COUNT(*) FROM RegisteredSchoolsTable")
    suspend fun countSchools(): Int

    // Select all registered schools
    @Query("SELECT * FROM RegisteredSchoolsTable")
    suspend fun getAllRegisteredSchools(): List<RegisteredSchools>

    //SchoolBuildings
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SchoolBuildings::class)
    suspend fun insertSchoolBuildings(buildings: List<SchoolBuildings>)

    @Query("SELECT COUNT(*) FROM BuildingsTable")
    suspend fun countSchoolBuildings(): Int

    @Query("SELECT * FROM BuildingsTable")
    suspend fun getAllSchoolBuildings(): List<SchoolBuildings>

    //WorkOrders
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = WorkOrders::class)
    suspend fun insertWorkOrders(workOrder: List<WorkOrders>)

    @Query("SELECT COUNT(*) FROM WorkOrderMaster")
    suspend fun countWorkOrders(): Int

    @Query("SELECT * FROM WorkOrderMaster")
    suspend fun getAllWorkOrders(): List<WorkOrders>

    //For Local Data base Of Schools Survey
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = ImageData::class)
    suspend fun insertIntoLocalDb(surveyData: ImageData): Long

    @Query("SELECT * FROM ImageDataTable")
    suspend fun getAllImages(): List<ImageData>

    @Query("SELECT COUNT(*) FROM ImageDataTable")
    suspend fun getDbCount(): Int

    @Query("DELETE FROM ImageDataTable WHERE school_Name = :schoolName AND po_office = :poOffice")
    suspend fun deleteItems(schoolName: String, poOffice: String)

    //Local Data for timeline table
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = WorkOrderTimelineModel::class)
    suspend fun insertTimeLine(timeLineData: List<WorkOrderTimelineModel>)

    @Query("SELECT * FROM TimelineTable WHERE po_office =:poOffice")
    suspend fun getTimelines(poOffice: String): List<WorkOrderTimelineModel>
}
