package com.example.pwd_app.viewModel.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UploadViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> = _uploadStatus

    fun uploadData(
        school_Name: String,
        po_office: String,
        image_name: String,
        image_type: String,
        image_pdf: String,
        upload_date: String,
        upload_time: String,
        EntryBy: String,
        Latitude: String,
        Longitude: String,
        user_upload_date: String,
        InspectionType: String,
        WorkorderNumber: String,
        Description: String,
        ags: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = dataRepository.uploadData(
                    school_Name,
                    po_office,
                    image_name,
                    image_type,
                    image_pdf,
                    upload_date,
                    upload_time,
                    EntryBy,
                    Latitude,
                    Longitude,
                    user_upload_date,
                    InspectionType,
                    WorkorderNumber,
                    Description,
                    ags
                )

                // Handle the response as needed
                if (response.code() == 200) {
                    // Handle a successful response
                    _uploadStatus.postValue(true)
                } else {
                    // Handle an unsuccessful response
                    _uploadStatus.postValue(false)
                }
            } catch (e: Exception) {
                // Handle network or other errors
                Log.d("ERROR", e.toString())
                _uploadStatus.postValue(false)
            }
        }
    }
}