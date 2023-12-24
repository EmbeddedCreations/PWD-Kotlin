package com.example.pwd_app.viewModel.mapActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.model.RegisteredSchools
import com.example.pwd_app.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel (private val homeRepository: HomeRepository) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getSchools()
        }
    }
    val schools: LiveData<List<RegisteredSchools>>
        get() = homeRepository.schools
}