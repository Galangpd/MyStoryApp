package com.example.mystoryapp

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mystoryapp.api.ApiService
import com.example.mystoryapp.response.ListStoryItem
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class Paging(
    private val token: String,
    private val apiService: ApiService,
) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        // Menentukan posisi pertama yang akan dimuat ketika refresh
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
//            val token = userPreference.getUser().first().token
            val response = apiService.getStories("Bearer $token", position, params.loadSize)

            LoadResult.Page(
                data = response.listStory,
                prevKey = if(position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.listStory.isEmpty()) null else position + 1
            )
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        private const val INITIAL_PAGE_INDEX = 1
    }
}