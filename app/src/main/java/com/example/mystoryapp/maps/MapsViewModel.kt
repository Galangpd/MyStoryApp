package com.example.mystoryapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.Result
import com.example.mystoryapp.UserModel
import com.example.mystoryapp.repository.Repository
import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.response.StoryResponse

class MapsViewModel (private val repository: Repository): ViewModel() {
    val StoryLocation: LiveData<StoryResponse> = repository.StoryLocation

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

//    fun getStoriesWithLocation(token: String): LiveData<StoryResponse> {
//        return repository.getStoryWithLocation(token, 1)
//    }

    fun getStoryWithLocation(token: String){
        repository.storyWithLocation(token)
    }

}