package com.example.mystoryapp.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.response.RegisterResponse
import com.example.mystoryapp.repository.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    private val _registrationResult = MutableLiveData<RegisterResponse>()
    val registrationResult: LiveData<RegisterResponse> get() = _registrationResult

    fun registerUser(name: String, email: String, password: String) = repository.register(name, email, password)
}