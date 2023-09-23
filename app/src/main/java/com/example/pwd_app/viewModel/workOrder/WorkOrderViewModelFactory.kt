package com.example.pwd_app.viewModel.workOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.repository.HomeRepository
import com.example.pwd_app.repository.TimeLineRepository

class WorkOrderViewModelFactory(private val timeLineRepository: TimeLineRepository,private val homeRepository: HomeRepository): ViewModelProvider.Factory {
    override fun<T : ViewModel> create(modelClass: Class<T>): T {
        return WorkOrderViewModel(timeLineRepository,homeRepository) as T
    }
}