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
    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJob2FuZ3RpZW4yazMuY29tIiwic3ViIjoicXVvY2Jhb2R0cjcwIiwiZXhwIjoxNzQ3ODE1NzE5LCJpYXQiOjE3NDUyMjM3MTksInNjb3BlIjoiQURNSU4ifQ.pWkwdCD6vOXxvS_hWG23qkyl6EgIBqdOqB2_vqHFnRGJCZyHiuojPt8ZwRjqIAot8BtxOpN897QqDo66-VGq7A"
    fun updateUserInfo(name: String, email: String, password: String) {
        val updateUserRequest = UpdateUserRequest(name, email, password)
        val userId = DataLocalManager.getInstance().getInfoUserId()
        performAction(_updateUser) {
            apiService.updateUser(userId, updateUserRequest, token)
        }
    }
}