package com.example.news_backend.ui.menu.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.network.ApiResponse
import com.example.news_backend.network.UserResponse
import com.example.news_backend.network.request.ChangePasswordRequest
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Resource

class ChangePasswordViewModel : BaseViewModelImpl() {
    private val _changePasswordResult = MutableLiveData<Resource<ApiResponse<UserResponse>>>()
    val changePasswordResult: LiveData<Resource<ApiResponse<UserResponse>>> = _changePasswordResult

    val apiService = RetrofitBase.apiService(AppNewsService::class.java)

    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        val changePasswordRequest = ChangePasswordRequest(oldPassword, newPassword, confirmPassword)
        performAction(_changePasswordResult) {
            apiService.changePassword(changePasswordRequest)
        }
    }
}