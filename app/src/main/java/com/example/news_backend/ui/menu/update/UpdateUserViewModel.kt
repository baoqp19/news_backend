package com.example.news_backend.ui.menu.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.network.ApiResponse
import com.example.news_backend.network.UserResponse
import com.example.news_backend.network.request.UpdateUserRequest
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Resource

class UpdateUserViewModel : BaseViewModelImpl() {
    private val _updateUser = MutableLiveData<Resource<ApiResponse<UserResponse>>>()
    val updateUser: LiveData<Resource<ApiResponse<UserResponse>>> = _updateUser

    val apiService = RetrofitBase.apiService(AppNewsService::class.java)

    fun updateUserInfo(name: String, username: String, email: String, password: String) {
        val updateUserRequest = UpdateUserRequest(name, email, password)
        val userId = DataLocalManager.getInstance().getInfoUserId()
        performAction(_updateUser) {
            apiService.updateUser(userId, updateUserRequest)
        }
    }
}