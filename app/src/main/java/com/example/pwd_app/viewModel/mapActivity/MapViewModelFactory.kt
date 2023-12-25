package com.example.pwd_app.viewModel.mapActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.repository.HomeRepository
import com.example.pwd_app.repository.MapRepository

class MapViewModelFactory(private val mapRepository: MapRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(mapRepository) as T
    }
}