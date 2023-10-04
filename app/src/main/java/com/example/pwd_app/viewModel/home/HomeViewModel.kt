package com.example.pwd_app.viewModel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.model.RegisteredSchools
import com.example.pwd_app.model.SchoolBuildings
import com.example.pwd_app.model.WorkOrders
import com.example.pwd_app.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getSchools()
            homeRepository.getBuildings()

        }
    }
    fun getWorkOrder(){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getWorkOrders()
        }
    }

    val schools: LiveData<List<RegisteredSchools>>
        get() = homeRepository.schools
    val buildings: LiveData<List<SchoolBuildings>>
        get() = homeRepository.buildings
    val workOrders: LiveData<List<WorkOrders>>
        get() = homeRepository.workOrders
}