package com.example.pwd_app.viewModel.localDbView

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.model.ImageData

import com.example.pwd_app.repository.LocalDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalDbViewModel(
    private val localDatabaseRepository: LocalDatabaseRepository
    ): ViewModel() {
    init{
        viewModelScope.launch (Dispatchers.IO){
            localDatabaseRepository.getData()
        }
    }
    val surveyData : LiveData<List<ImageData>>
        get() = localDatabaseRepository.images

//    suspend fun deleteData(imageData: ImageData){
//        localDatabaseRepository.deleteImageData(imageData)
//    }
}