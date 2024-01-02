package com.example.pwd_app.viewModel.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.data.remote.ApiInterface

class WorkCardViewModelFactory(private val apiInterface: ApiInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass : Class<T>) : T{
        return WorkCardViewModel(apiInterface) as T
    }
}