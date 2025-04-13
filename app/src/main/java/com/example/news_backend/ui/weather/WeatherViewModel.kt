package com.example.news_backend.ui.weather

import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitWeatherBase
import com.example.news_backend.network.response.WeatherResponse
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Constants
import com.example.news_backend.utils.Resource

class WeatherViewModel : BaseViewModelImpl() {
    private val _weatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    val weatherResponse: MutableLiveData<Resource<WeatherResponse>> = _weatherResponse

    val apiService = RetrofitWeatherBase.apiService(AppNewsService::class.java)

    fun getCurrentWeatherData(city: String) {
        val apiKey = Constants.apiKeyWeather
        performAction(_weatherResponse) {
            apiService.getCurrentWeather(city, apiKey)
        }
    }
}
