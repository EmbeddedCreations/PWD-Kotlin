package com.example.pwd_app.viewModel.workOrder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.UploadTimelineModel
import com.example.pwd_app.model.WorkOrderTimelineModel
import com.example.pwd_app.model.WorkOrders
import com.example.pwd_app.repository.HomeRepository
import com.example.pwd_app.repository.TimeLineRepository
import com.example.pwd_app.repository.UploadTimelineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body

class WorkOrderViewModel(
    private val timeLineRepository: TimeLineRepository,
    private val homeRepository: HomeRepository,
    private val uploadTimelineRepository: UploadTimelineRepository
) : ViewModel() {

    // LiveData to track the loading state
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    //upload Status
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> = _uploadStatus

    // LiveData to store error messages if any
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun setWorkorderTimeline(@Body workOrderData: UploadTimelineModel?){
        viewModelScope.launch (Dispatchers.IO){
            try{
                val response = uploadTimelineRepository.setTimeline(workOrderData)
                // Handle the response as needed
                if (response.code() == 200) {
                    // Handle a successful response
                    _uploadStatus.postValue(true)
                } else {
                    // Handle an unsuccessful response
                    _uploadStatus.postValue(false)
                }
            }catch (e: Exception) {
                // Handle network or other errors
                Log.d("ERROR", e.toString())
                //_uploadStatus.postValue(false)
            }
        }
    }

    fun fetchTimeline() {
        _loading.value = true // Set loading to true before fetching data
        viewModelScope.launch {
            try {
                // Fetch data in the background
                val timelineResult = withContext(Dispatchers.IO) {
                    timeLineRepository.getTimeline(Credentials.DEFAULT_PO)
                }

                // Check if the fetching was successful
                if (timelineResult != null) {
                    // Fetch work orders in the background
                    val workOrdersResult = withContext(Dispatchers.IO) {
                        homeRepository.getWorkOrders()
                    }

                    // Check if fetching work orders was successful
                    if (workOrdersResult != null) {
                        // Data fetching is complete, set loading to false
                        _loading.value = false
                    } else {
                        // There was an error fetching work orders, set an error message
                        _errorMessage.value = "Error fetching work orders"
                    }
                } else {
                    // There was an error fetching timeline data, set an error message
                    _errorMessage.value = "Error fetching timeline data"
                }
            } catch (e: Exception) {
                // Handle any exceptions that occur during data fetching
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    val workOrders: LiveData<List<WorkOrders>>
        get() = homeRepository.workOrders

    val timeLine: LiveData<List<WorkOrderTimelineModel>>
        get() = timeLineRepository.timeLine
}
