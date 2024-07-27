package com.example.mystoryapp.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.UserModel
import com.example.mystoryapp.repository.Repository
import com.example.mystoryapp.response.StoryResponse
import kotlinx.coroutines.launch
import retrofit2.await
import java.io.File

class UploadStoryViewModel (
    private val repository: Repository,
) : ViewModel(){
    private val _responseLiveData = MutableLiveData<StoryResponse>()

    private val _successLiveData = MutableLiveData<Boolean>()
    val successLiveData: LiveData<Boolean> get() = _successLiveData

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun uploadStory(token: String, description: String, photoFile: File) {
        viewModelScope.launch {
            try {
                val response =
                    repository.uploadStory(token, description, photoFile).await()
                _responseLiveData.value = response
                _successLiveData.value = true
            } catch (_: Exception) {
            }
        }
    }
}