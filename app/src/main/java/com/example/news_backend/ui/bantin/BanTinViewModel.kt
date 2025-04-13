package com.example.news_backend.ui.bantin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.models.BanTin
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.network.ApiResponse
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Constants
import com.example.news_backend.utils.Resource

class BanTinViewModel : BaseViewModelImpl() {
    private val _banTinNews = MutableLiveData<Resource<ApiResponse<List<BanTin>>>>()
    val listTinTuc: LiveData<Resource<ApiResponse<List<BanTin>>>> = _banTinNews

    val apiService = RetrofitBase.apiService(AppNewsService::class.java)

    fun fetchDataCallAPI(banTin: String) {
        if (banTin == Constants.full) {
            performAction(_banTinNews) {
                apiService.getFullBanTin()
            }
        }
        performAction(_banTinNews) {
            apiService.getBanTin(banTin)
        }
    }
}


