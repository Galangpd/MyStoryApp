package com.example.mystoryapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp.Paging
import com.example.mystoryapp.response.LoginResponse
import com.example.mystoryapp.Preference
import com.example.mystoryapp.response.RegisterResponse
import com.example.mystoryapp.UserModel
import com.example.mystoryapp.api.ApiService
import com.example.mystoryapp.Result
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class Repository private constructor(
    private val userPreference: Preference,
    private val apiService: ApiService
) {

    private val _StoryLocation = MutableLiveData<StoryResponse>()
    val StoryLocation: LiveData<StoryResponse> = _StoryLocation

    fun register(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.register(name, email, password)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(error.message?.let { Result.Error(it) })
        }
    }

    fun loginUser(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.login(email, password)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(error.message?.let { Result.Error(it) })
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getUser()
    }

    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                Paging(token, apiService)
            }
        ).liveData
    }

    fun uploadStory(token: String, description: String, photoFile: File, ): Call<StoryResponse> {
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData(
            "photo", photoFile.name, photoFile.asRequestBody("image/*".toMediaTypeOrNull())
        )
        return apiService.uploadStory("Bearer $token", descriptionPart, photoPart)
    }

    fun storyWithLocation(token: String){
        val client = apiService.getStoriesLocation("Bearer $token")
        client.enqueue(object: Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _StoryLocation.value = response.body()
                } else {
                    Log.e("getStory","onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e("getStory", "onFailure: ${t.message.toString()}")
            }
        })
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: Preference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}
