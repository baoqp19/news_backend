package com.example.news_backend.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.network.request.PostNewsLetterRequest
import com.example.news_backend.network.response.PostNewsLetterResponse
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Resource

class PostNewsLetterViewModel : BaseViewModelImpl() {
    private val _postNewsLetterResult = MutableLiveData<Resource<PostNewsLetterResponse>>()
    val postNewsLetterResult: LiveData<Resource<PostNewsLetterResponse>> = _postNewsLetterResult

    val apiService = RetrofitBase.apiService(AppNewsService::class.java)

    fun postNewsLetter(title: String, link: String, img: String, pubDate: String, category: String) {
        val postNewsLetterRequest = PostNewsLetterRequest(title, link, img, pubDate, category)
        performAction(_postNewsLetterResult) {
            apiService.postNews(postNewsLetterRequest)
        }
    }

}