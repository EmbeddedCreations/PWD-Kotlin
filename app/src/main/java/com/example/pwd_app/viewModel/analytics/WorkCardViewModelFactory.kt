package com.example.pwd_app.viewModel.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.repository.UploadWorkLogRepository

class WorkCardViewModelFactory(private val apiInterface: ApiInterface,private val uploadWorkLogRepository: UploadWorkLogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass : Class<T>) : T{
        return WorkCardViewModel(apiInterface,uploadWorkLogRepository) as T
    }
}