package com.example.pwd_app.data.remote

import com.example.pwd_app.model.ImageData
import com.example.pwd_app.model.LoginCredentials
import com.example.pwd_app.model.RegisteredSchools
import com.example.pwd_app.model.SchoolBuildings
import com.example.pwd_app.model.SchoolData
import com.example.pwd_app.model.UpdateResponse
import com.example.pwd_app.model.UploadTimelineModel
import com.example.pwd_app.model.WorkOrderTimelineModel
import com.example.pwd_app.model.WorkOrders
import com.example.pwd_app.model.WorkorderLog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiInterface {

    @GET("app_login_pwd.php")
    suspend fun getUserCredentials(): Response<List<LoginCredentials>>

    @GET("app_school_select.php")
    suspend fun getRegisteredSchools(
        @Query("atc_office") atc_office: String,
        @Query("po_office") po_office: String
    ): Response<List<RegisteredSchools>>

    @GET("app_workOrderSurvey.php")
    suspend fun getWorks(
        @Query("juniorEngineer") juniorEngineer : String
    ): Response<List<WorkorderLog>>

    @GET("app_building_select.php")
    suspend fun getSchoolBuildings(): Response<List<SchoolBuildings>>

    @GET("app_fetch_workorder.php")
    suspend fun getWorkOrders(
        @Query("atc_office") atc_office: String
    ): Response<List<WorkOrders>>

    @GET("appFetchSchools.php")
    suspend fun getSchools(
        @Query("user") user: String
    ): Response<List<SchoolData>>

    @GET("appFetchSchoolBuildings.php")
    suspend fun getBuildings(
        @Query("user") user: String,
        @Query("school") school: String
    ): Response<List<SchoolData>>

    @GET("app_fetch_workOrderTimeline.php")
    suspend fun getWorkOrderTimeline(
        @Query("po_office") po_office: String,
    ): Response<List<WorkOrderTimelineModel>>

    @GET("app_registeredSchools.php")
    suspend fun getSchoolCoordinates(): Response<List<RegisteredSchools>>

    @FormUrlEncoded
    @POST("app_Login_Log.php")
    suspend fun loginLog(
        @Field("poOfficeName") poOfficeName : String,
        @Field("jrName") jrName : String
    ):Response<UpdateResponse>

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
    ): Response<UpdateResponse>

    @FormUrlEncoded
    @POST("AppUpdateBuilding.php")
    suspend fun EditData(
        @Field("id") id: String,
        @Field("school_Name") schoolName: String,
        @Field("po_office") poOffice: String,
        @Field("image_name") imageName: String,
        @Field("EntryBy") entryBy: String,
        @Field("Description") description: String,
    ): Response<UpdateResponse>
    @FormUrlEncoded
    @POST("AppUpdateTimeline.php")
    suspend fun updateWorkOrder(
        @Field("workorder_no") workorderNo: String,
        @Field("itemofwork") itemOfWork: String,
        @Field("countofweek") countOfWeek: String,
        @Field("SelWeek1") selWeek1: String,
        @Field("SelWeek2") selWeek2: String,
        @Field("SelWeek3") selWeek3: String,
        @Field("SelWeek4") selWeek4: String,
        @Field("SelWeek5") selWeek5: String,
        @Field("SelWeek6") selWeek6: String,
        @Field("SelWeek7") selWeek7: String,
        @Field("SelWeek8") selWeek8: String,
        @Field("SelWeek9") selWeek9: String,
        @Field("SelWeek10") selWeek10: String,
        @Field("SelWeek11") selWeek11: String,
        @Field("SelWeek12") selWeek12: String,
        @Field("SelWeek13") selWeek13: String,
        @Field("SelWeek14") selWeek14: String,
        @Field("SelWeek15") selWeek15: String,
        @Field("SelWeek16") selWeek16: String,
        @Field("SelWeek17") selWeek17: String,
        @Field("SelWeek18") selWeek18: String,
        @Field("SelWeek19") selWeek19: String,
        @Field("SelWeek20") selWeek20: String,
        @Field("SelWeek21") selWeek21: String,
        @Field("SelWeek22") selWeek22: String,
        @Field("SelWeek23") selWeek23: String,
        @Field("SelWeek24") selWeek24: String,
        @Field("SelWeek25") selWeek25: String,
        @Field("SelWeek26") selWeek26: String,
        @Field("SelWeek27") selWeek27: String,
        @Field("SelWeek28") selWeek28: String,
        @Field("SelWeek29") selWeek29: String,
        @Field("SelWeek30") selWeek30: String,
        @Field("SelWeek31") selWeek31: String,
        @Field("SelWeek32") selWeek32: String,
        @Field("SelWeek33") selWeek33: String,
        @Field("SelWeek34") selWeek34: String,
        @Field("SelWeek35") selWeek35: String,
        @Field("SelWeek36") selWeek36: String,
        @Field("SelWeek37") selWeek37: String,
        @Field("SelWeek38") selWeek38: String,
        @Field("SelWeek39") selWeek39: String,
        @Field("SelWeek40") selWeek40: String,
        @Field("SelWeek41") selWeek41: String,
        @Field("SelWeek42") selWeek42: String,
        @Field("SelWeek43") selWeek43: String,
        @Field("SelWeek44") selWeek44: String,
        @Field("SelWeek45") selWeek45: String,
        @Field("SelWeek46") selWeek46: String,
        @Field("SelWeek47") selWeek47: String,
        @Field("SelWeek48") selWeek48: String,
        @Field("SelWeek49") selWeek49: String,
        @Field("SelWeek50") selWeek50: String,
        @Field("SelWeek51") selWeek51: String,
        @Field("SelWeek52") selWeek52: String,
        @Field("SelWeek53") selWeek53: String,
        @Field("SelWeek54") selWeek54: String,
        @Field("SelWeek55") selWeek55: String,
        @Field("SelWeek56") selWeek56: String,
        @Field("SelWeek57") selWeek57: String,
        @Field("SelWeek58") selWeek58: String,
        @Field("SelWeek59") selWeek59: String,
        @Field("SelWeek60") selWeek60: String,
        @Field("SelWeek61") selWeek61: String,
        @Field("SelWeek62") selWeek62: String,
        @Field("SelWeek63") selWeek63: String,
        @Field("SelWeek64") selWeek64: String,
        @Field("SelWeek65") selWeek65: String,
        @Field("SelWeek66") selWeek66: String,
        @Field("SelWeek67") selWeek67: String,
        @Field("SelWeek68") selWeek68: String,
        @Field("SelWeek69") selWeek69: String,
        @Field("SelWeek70") selWeek70: String,
        @Field("SelWeek71") selWeek71: String,
        @Field("SelWeek72") selWeek72: String,
        @Field("SelWeek73") selWeek73: String,
        @Field("SelWeek74") selWeek74: String,
        @Field("SelWeek75") selWeek75: String,
        @Field("SelWeek76") selWeek76: String,
        @Field("SelWeek77") selWeek77: String,
        @Field("SelWeek78") selWeek78: String,
        @Field("SelWeek79") selWeek79: String,
        @Field("SelWeek80") selWeek80: String,
        @Field("SelWeek81") selWeek81: String,
        @Field("SelWeek82") selWeek82: String,
        @Field("SelWeek83") selWeek83: String,
        @Field("SelWeek84") selWeek84: String,
        @Field("SelWeek85") selWeek85: String,
        @Field("SelWeek86") selWeek86: String,
        @Field("SelWeek87") selWeek87: String,
        @Field("SelWeek88") selWeek88: String,
        @Field("SelWeek89") selWeek89: String,
        @Field("SelWeek90") selWeek90: String,
        @Field("SelWeek91") selWeek91: String,
        @Field("SelWeek92") selWeek92: String,
        @Field("SelWeek93") selWeek93: String,
        @Field("SelWeek94") selWeek94: String,
        @Field("SelWeek95") selWeek95: String,
        @Field("SelWeek96") selWeek96: String
    ): Response<UpdateResponse>
}