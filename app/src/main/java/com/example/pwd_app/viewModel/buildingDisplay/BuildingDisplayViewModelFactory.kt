package com.example.pwd_app.viewModel.buildingDisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.viewModel.schoolDisplay.SchoolDisplayViewModel

class BuildingDisplayViewModelFactory(private val apiInterface: ApiInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BuildingDisplayViewModel(apiInterface) as T
    }
}