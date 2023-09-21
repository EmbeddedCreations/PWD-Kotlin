package com.example.pwd_app.viewModel.buildingDisplay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.SchoolData
import kotlinx.coroutines.launch

class BuildingDisplayViewModel(
    private val apiInterface: ApiInterface
) : ViewModel() {
    private val _schoolData = MutableLiveData<List<SchoolData>>()
    val schoolList: LiveData<List<SchoolData>> get() = _schoolData
    fun fetchBuildingData() {
        viewModelScope.launch {
            try {
                val response = apiInterface.getBuildings(
                    Credentials.DEFAULT_JUNIOR_ENGINEER,
                    Credentials.SELECTED_SCHOOL_FOR_DISPLAY
                )
                _schoolData.postValue(response.body())
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }
}