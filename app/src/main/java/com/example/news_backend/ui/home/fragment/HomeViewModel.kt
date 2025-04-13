package com.example.news_backend.ui.home.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.network.ApiResponse
import com.example.news_backend.network.UserResponse
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Resource
import retrofit2.Call

class HomeViewModel : BaseViewModelImpl() {
    private val _accessTokenKey = MutableLiveData<Resource<ApiResponse<UserResponse>>>()
    val accessTokenKey: LiveData<Resource<ApiResponse<UserResponse>>> = _accessTokenKey

    val apiService = RetrofitBase.apiService(AppNewsService::class.java)

    init {
        this.fetchDataCallAPI()
    }

    private fun callApiMyInfo(): Call<ApiResponse<UserResponse>> {
        return apiService.myinfo()
    }

    private fun fetchDataCallAPI() {
        performAction(_accessTokenKey) {
            callApiMyInfo()
        }
    }
}