package com.example.pwd_app.viewModel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.model.LoginCredentials
import com.example.pwd_app.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.getUsers()
        }
    }

    val users: LiveData<List<LoginCredentials>>
        get() = loginRepository.users
}