package com.example.pwd_app.viewModel.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.WorkorderLog
import com.example.pwd_app.repository.UploadWorkLogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkCardViewModel(
    private val apiInterface: ApiInterface,
    private val uploadWorkLogRepository: UploadWorkLogRepository
) : ViewModel(){
    private val _workData = MutableLiveData<List<WorkorderLog>>()
    private val _editStatus =MutableLiveData<Boolean>()
    val editStatus: LiveData<Boolean> = _editStatus
    val workList: LiveData<List<WorkorderLog>> get() = _workData

    fun fetchWorkOrders(){
        viewModelScope.launch{
            try{
                val response = apiInterface.getWorks(
                    Credentials.DEFAULT_JUNIOR_ENGINEER
                )
                _workData.postValue(response.body())
            }catch(e: Exception){
                // Handle Exceptions
            }
        }
    }

    fun updateLog(
        id : String,
        assignedJE : String,
        weekNumber : String,
        isWorkOnTime : String,
        isPhotoUploaded : String,
        sitePhysicalVisit : String,
        amountReleased : String,
        progressRating :String,
        weeklyProgressUpdateStatus : String
    ){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = uploadWorkLogRepository.updateLog(
                    id,
                    assignedJE,
                    weekNumber,
                    isWorkOnTime,
                    isPhotoUploaded,
                    sitePhysicalVisit,
                    amountReleased,
                    progressRating,
                    weeklyProgressUpdateStatus
                )
                if(response.code() ==  200){
                    _editStatus.postValue(true)
                }else{
                    _editStatus.postValue(false)
                }
            }catch (e : Exception){
                _editStatus.postValue(true)
            }
        }
    }
}