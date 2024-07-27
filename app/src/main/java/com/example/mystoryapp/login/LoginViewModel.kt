package com.example.mystoryapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.UserModel
import com.example.mystoryapp.repository.Repository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun loginUser(email: String, password: String) = repository.loginUser(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}