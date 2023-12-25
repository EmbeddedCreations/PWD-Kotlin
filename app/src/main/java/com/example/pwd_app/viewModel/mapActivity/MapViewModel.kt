package com.example.pwd_app.viewModel.mapActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.model.RegisteredSchools
import com.example.pwd_app.repository.HomeRepository
import com.example.pwd_app.repository.MapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel (private val mapRepository: MapRepository) : ViewModel() {

    fun fetchSchools(){
        viewModelScope.launch(Dispatchers.IO) {
            mapRepository.getSchools()
        }
    }

    val schools: LiveData<List<RegisteredSchools>>
        get() = mapRepository.schools
}