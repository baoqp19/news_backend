package com.example.news_backend.ui.menu.delete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Resource

class DeleteUserViewModel : BaseViewModelImpl() {
    private val _deleteUser = MutableLiveData<Resource<String>>()
    val deleteUser: LiveData<Resource<String>> = _deleteUser

    val apiService = RetrofitBase.apiService(AppNewsService::class.java)

    fun deleteUserId(id: Long) {
        performAction(_deleteUser) {
            apiService.delete(id)
        }
    }

}