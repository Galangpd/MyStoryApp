package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.Preference
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.dataStore
import com.example.mystoryapp.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
    val pref = Preference.getInstance(context.dataStore)
    val apiService = ApiConfig.getApiService()
    return Repository.getInstance(pref,apiService)
    }
}