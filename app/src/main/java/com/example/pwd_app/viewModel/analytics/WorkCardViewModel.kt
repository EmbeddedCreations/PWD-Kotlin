package com.example.pwd_app.viewModel.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.WorkOrders
import kotlinx.coroutines.launch

class WorkCardViewModel(
    private val apiInterface: ApiInterface
) : ViewModel(){
    private val _workData = MutableLiveData<List<WorkOrders>>()

    val workList: LiveData<List<WorkOrders>> get() = _workData

    fun fetchWorkOrders(){
        viewModelScope.launch{
            try{
                val response = apiInterface.getWorks(
                    Credentials.DEFAULT_JUNIOR_ENGINEER
                )
                _workData.postValue(response.body())
            }catch(e: Exception){
                // Handle Exceptions
            }
        }
    }
}