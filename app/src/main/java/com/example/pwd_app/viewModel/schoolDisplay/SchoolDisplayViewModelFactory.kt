package com.example.pwd_app.viewModel.schoolDisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.viewModel.home.HomeViewModel

class SchoolDisplayViewModelFactory(private val apiInterface: ApiInterface) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SchoolDisplayViewModel(apiInterface) as T
    }
}
