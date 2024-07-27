package com.example.mystoryapp

data class UserModel (
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)