package com.example.news_backend.ui.football

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.models.Football
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.network.ApiResponse
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Resource
import retrofit2.Call


class FootballViewModel : BaseViewModelImpl() {
    private val _footballNews = MutableLiveData<Resource<ApiResponse<List<Football>>>>()
    val footballNews: LiveData<Resource<ApiResponse<List<Football>>>> = _footballNews

    val apiService = RetrofitBase.apiService(AppNewsService::class.java)

    init {
        this.fetchDataCallAPI()
    }

    private fun callApiFootball(): Call<ApiResponse<List<Football>>> {
        return apiService.getFootballData()
    }

    fun fetchDataCallAPI() {
        performAction(_footballNews) {
            callApiFootball()
        }
    }

}
