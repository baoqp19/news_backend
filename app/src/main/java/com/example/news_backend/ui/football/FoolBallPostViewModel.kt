package com.example.news_backend.ui.football


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.network.request.FoolBallRequest
import com.example.news_backend.network.request.FootballDto
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Resource


class FootballPostViewModel : BaseViewModelImpl() {

    private val _postResult = MutableLiveData<Resource<FootballDto>>()
    val postResult: LiveData<Resource<FootballDto>> = _postResult


    val apiService = RetrofitBase.apiService(AppNewsService::class.java)


    fun postFootballNews(request: FoolBallRequest) {
        performAction(_postResult) {
            apiService.postFootball(request)
        }
    }
}