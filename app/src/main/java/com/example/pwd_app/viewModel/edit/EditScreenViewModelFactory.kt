package com.example.pwd_app.viewModel.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.repository.UploadRepository

class EditScreenViewModelFactory(private val uploadRepository: UploadRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditScreenViewModel(uploadRepository) as T
    }
}