package com.example.pwd_app.viewModel.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.repository.DataRepository

class UploadViewModelFactory(private val dataRepository: DataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UploadViewModel(dataRepository) as T
    }
}