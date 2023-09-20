package com.example.pwd_app.viewModel.schoolDisplay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.SchoolData
import kotlinx.coroutines.launch

class SchoolDisplayViewModel(
    private val apiInterface: ApiInterface
) : ViewModel() {
    private val _schoolData = MutableLiveData<List<SchoolData>>()
    val schoolList: LiveData<List<SchoolData>> get() = _schoolData

    fun fetchSchoolData() {
        viewModelScope.launch {
            try {
                val response = apiInterface.getSchools(Credentials.DEFAULT_JUNIOR_ENGINEER)
                _schoolData.postValue(response.body())
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }
}