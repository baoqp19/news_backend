package com.example.news_backend.ui.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.network.request.SavePosRequest
import com.example.news_backend.network.response.SavePosResponse
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Resource

class SaveBanTinViewModel : BaseViewModelImpl() {
    private val _saveBanTin = MutableLiveData<Resource<SavePosResponse>>()
    val saveBanTin: LiveData<Resource<SavePosResponse>> = _saveBanTin

    private val _getSaveBanTin = MutableLiveData<Resource<List<SavePosResponse>>>()
    val getSaveBanTin: LiveData<Resource<List<SavePosResponse>>> = _getSaveBanTin

    private val _deleteAllBanTin = MutableLiveData<Resource<Void>>()
    val deleteAllBanTin: LiveData<Resource<Void>> = _deleteAllBanTin

    val apiService = RetrofitBase.apiService(AppNewsService::class.java)


    // save Ban Tin call backend
    fun postNewsSave(title: String, link: String, img: String, pubDate: String, userId: Long) {
        val saveBanTinRequest = SavePosRequest(title, link, img, pubDate, userId)
        performAction(_saveBanTin) {
            apiService.postNewsSave(saveBanTinRequest)
        }
    }

    // get bản tin call backend local
    fun getListAllNewsSave() {
        performAction(_getSaveBanTin) {
            val userId = DataLocalManager.getInstance().getInfoUserId()
            apiService.getAllNewsSaveByUserId(userId)
        }
    }

    // delete all bản tin đã đọc
    fun deleteAllListNewsSave() {
        performAction(_deleteAllBanTin) {
            apiService.deleteAllPostNews()
        }
    }

}