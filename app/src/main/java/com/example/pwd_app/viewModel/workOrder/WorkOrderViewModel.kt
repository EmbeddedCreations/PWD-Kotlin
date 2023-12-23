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

    var currentFragmentTag: String? = null

    //upload Status
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> = _uploadStatus

    // LiveData to store error messages if any
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun setWorkorderTimeline(workorderNo: String,
                             itemOfWork: String,
                             countOfWeek: String,
                             selWeek1: String,
                             selWeek2: String,
                             selWeek3: String,
                             selWeek4: String,
                             selWeek5: String,
                             selWeek6: String,
                             selWeek7: String,
                             selWeek8: String,
                             selWeek9: String,
                             selWeek10: String,
                             selWeek11: String,
                             selWeek12: String,
                             selWeek13: String,
                             selWeek14: String,
                             selWeek15: String,
                             selWeek16: String,
                             selWeek17: String,
                             selWeek18: String,
                             selWeek19: String,
                             selWeek20: String,
                             selWeek21: String,
                             selWeek22: String,
                             selWeek23: String,
                             selWeek24: String,
                             selWeek25: String,
                             selWeek26: String,
                             selWeek27: String,
                             selWeek28: String,
                             selWeek29: String,
                             selWeek30: String,
                             selWeek31: String,
                             selWeek32: String,
                             selWeek33: String,
                             selWeek34: String,
                             selWeek35: String,
                             selWeek36: String,
                             selWeek37: String,
                             selWeek38: String,
                             selWeek39: String,
                             selWeek40: String,
                             selWeek41: String,
                             selWeek42: String,
                             selWeek43: String,
                             selWeek44: String,
                             selWeek45: String,
                             selWeek46: String,
                             selWeek47: String,
                             selWeek48: String,
                             selWeek49: String,
                             selWeek50: String,
                             selWeek51: String,
                             selWeek52: String,
                             selWeek53: String,
                             selWeek54: String,
                             selWeek55: String,
                             selWeek56: String,
                             selWeek57: String,
                             selWeek58: String,
                             selWeek59: String,
                             selWeek60: String,
                             selWeek61: String,
                             selWeek62: String,
                             selWeek63: String,
                             selWeek64: String,
                             selWeek65: String,
                             selWeek66: String,
                             selWeek67: String,
                             selWeek68: String,
                             selWeek69: String,
                             selWeek70: String,
                             selWeek71: String,
                             selWeek72: String,
                             selWeek73: String,
                             selWeek74: String,
                             selWeek75: String,
                             selWeek76: String,
                             selWeek77: String,
                             selWeek78: String,
                             selWeek79: String,
                             selWeek80: String,
                             selWeek81: String,
                             selWeek82: String,
                             selWeek83: String,
                             selWeek84: String,
                             selWeek85: String,
                             selWeek86: String,
                             selWeek87: String,
                             selWeek88: String,
                             selWeek89: String,
                             selWeek90: String,
                             selWeek91: String,
                             selWeek92: String,
                             selWeek93: String,
                             selWeek94: String,
                             selWeek95: String,
                             selWeek96: String){
        viewModelScope.launch (Dispatchers.IO){
            try{
                val response = uploadTimelineRepository.setTimeline(workorderNo,
                    itemOfWork,
                    countOfWeek,
                    selWeek1,
                    selWeek2,
                    selWeek3,
                    selWeek4,
                    selWeek5,
                    selWeek6,
                    selWeek7,
                    selWeek8,
                    selWeek9,
                    selWeek10,
                    selWeek11,
                    selWeek12,
                    selWeek13,
                    selWeek14,
                    selWeek15,
                    selWeek16,
                    selWeek17,
                    selWeek18,
                    selWeek19,
                    selWeek20,
                    selWeek21,
                    selWeek22,
                    selWeek23,
                    selWeek24,
                    selWeek25,
                    selWeek26,
                    selWeek27,
                    selWeek28,
                    selWeek29,
                    selWeek30,
                    selWeek31,
                    selWeek32,
                    selWeek33,
                    selWeek34,
                    selWeek35,
                    selWeek36,
                    selWeek37,
                    selWeek38,
                    selWeek39,
                    selWeek40,
                    selWeek41,
                    selWeek42,
                    selWeek43,
                    selWeek44,
                    selWeek45,
                    selWeek46,
                    selWeek47,
                    selWeek48,
                    selWeek49,
                    selWeek50,
                    selWeek51,
                    selWeek52,
                    selWeek53,
                    selWeek54,
                    selWeek55,
                    selWeek56,
                    selWeek57,
                    selWeek58,
                    selWeek59,
                    selWeek60,
                    selWeek61,
                    selWeek62,
                    selWeek63,
                    selWeek64,
                    selWeek65,
                    selWeek66,
                    selWeek67,
                    selWeek68,
                    selWeek69,
                    selWeek70,
                    selWeek71,
                    selWeek72,
                    selWeek73,
                    selWeek74,
                    selWeek75,
                    selWeek76,
                    selWeek77,
                    selWeek78,
                    selWeek79,
                    selWeek80,
                    selWeek81,
                    selWeek82,
                    selWeek83,
                    selWeek84,
                    selWeek85,
                    selWeek86,
                    selWeek87,
                    selWeek88,
                    selWeek89,
                    selWeek90,
                    selWeek91,
                    selWeek92,
                    selWeek93,
                    selWeek94,
                    selWeek95,
                    selWeek96)
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
