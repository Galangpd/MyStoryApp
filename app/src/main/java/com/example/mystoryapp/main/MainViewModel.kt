package com.example.mystoryapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.Result
import com.example.mystoryapp.UserModel
import com.example.mystoryapp.repository.Repository
import com.example.mystoryapp.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
   fun getStories(token: String): LiveData<PagingData<ListStoryItem>> =
        repository.getAllStory(token).cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}