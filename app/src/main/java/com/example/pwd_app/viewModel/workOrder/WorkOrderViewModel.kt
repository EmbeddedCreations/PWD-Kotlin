package com.example.pwd_app.viewModel.workOrder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.WorkOrderTimelineModel
import com.example.pwd_app.model.WorkOrders
import com.example.pwd_app.repository.HomeRepository
import com.example.pwd_app.repository.TimeLineRepository
import kotlinx.coroutines.launch

class WorkOrderViewModel(
    private val timeLineRepository: TimeLineRepository,
    private val homeRepository: HomeRepository
):ViewModel() {

    fun fetchTimeline(){
        viewModelScope.launch{
            timeLineRepository.getTimeline(Credentials.DEFAULT_PO)
            homeRepository.getWorkOrders()
        }
    }

    init{
        viewModelScope.launch{
            timeLineRepository.getTimeline(Credentials.DEFAULT_PO)
            homeRepository.getWorkOrders()
        }
    }
    val workOrders : LiveData<List<WorkOrders>>
        get() = homeRepository.workOrders

    val timeLine : LiveData<List<WorkOrderTimelineModel>>
        get() = timeLineRepository.timeLine


}