package com.example.news_backend.ui.base

import androidx.lifecycle.MutableLiveData
import com.example.news_backend.utils.Resource
import retrofit2.Call

interface BaseViewModel {
    fun <T> performAction(resultLiveData: MutableLiveData<Resource<T>>, apiCall: () -> Call<T>)
}