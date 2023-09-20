package com.example.pwd_app.viewModel.localDbView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.repository.LocalDatabaseRepository

class LocalDbViewModelFactory(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocalDbViewModel(localDatabaseRepository) as T
    }
}