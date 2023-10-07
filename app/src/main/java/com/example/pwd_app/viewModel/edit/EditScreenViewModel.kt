package com.example.pwd_app.viewModel.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.repository.UploadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditScreenViewModel(
    private val uploadRepository: UploadRepository
) : ViewModel() {
    private val _editStatus = MutableLiveData<Boolean>()
    val editStatus: LiveData<Boolean> = _editStatus

    fun editData(
        id: String,
        school_Name: String,
        po_office: String,
        image_name: String,
        EntryBy: String,
        Description: String,

        ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = uploadRepository.editData(
                    id,
                    school_Name,
                    po_office,
                    image_name,
                    EntryBy,
                    Description,
                )
                // Handle the response as needed
                if (response.code() == 200) {
                    // Handle a successful response
                    _editStatus.postValue(true)
                } else {
                    // Handle an unsuccessful response
                    _editStatus.postValue(false)
                }
            } catch (e: Exception) {
                // Handle network or other errors
                Log.d("ERROR", e.toString())
                //_editStatus.postValue(false)
            }
        }
    }
}