package com.example.pwd_app.data.remote

import com.example.pwd_app.model.ImageData
import com.example.pwd_app.model.SchoolBuildings
import com.example.pwd_app.model.LoginCredentials
import com.example.pwd_app.model.RegisteredSchools
import com.example.pwd_app.model.SchoolData
import com.example.pwd_app.model.WorkOrderTimelineModel
import com.example.pwd_app.model.WorkOrders
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface{

    @GET("app_login_pwd.php")
    suspend fun getUserCredentials(): Response<List<LoginCredentials>>

    @GET("app_school_select.php")
    suspend fun getRegisteredSchools(
        @Query("atc_office") atc_office: String,
        @Query("po_office") po_office: String
    ): Response<List<RegisteredSchools>>

    @GET("app_building_select.php")
    suspend fun getSchoolBuildings(): Response<List<SchoolBuildings>>

    @GET("app_fetch_workorder.php")
    suspend fun getWorkOrders(
        @Query("atc_office") atc_office: String
    ):Response<List<WorkOrders>>

    @GET("appFetchSchools.php")
    suspend fun getSchools(
        @Query("user") user: String
    ):Response<List<SchoolData>>

    @GET("appFetchSchoolBuildings.php")
    suspend fun getBuildings(
        @Query("user") user: String,
        @Query("school") school: String
    ):Response<List<SchoolData>>

    @GET("app_fetch_workOrderTimeline.php")
    suspend fun getWorkOrderTimeline(
        @Query("po_office") po_office: String,
    ):Response<List<WorkOrderTimelineModel>>

    @FormUrlEncoded
    @POST("app_upload_Image.php")
    suspend fun uploadData(
        @Field("school_Name") schoolName: String,
        @Field("po_office") poOffice: String,
        @Field("image_name") imageName: String,
        @Field("image_type") imageType: String,
        @Field("image_pdf") imagePdf: String,
        @Field("upload_date") uploadDate: String,
        @Field("upload_time") uploadTime: String,
        @Field("EntryBy") entryBy: String,
        @Field("Latitude") latitude: String,
        @Field("Longitude") longitude: String,
        @Field("user_upload_date") userUploadDate: String,
        @Field("InspectionType") inspectionType: String,
        @Field("WorkorderNumber") workorderNumber: String,
        @Field("Description") description: String,
        @Field("Tags") tags: String
    ): Response<ImageData>
}