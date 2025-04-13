package com.example.news_backend.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.network.ApiResponse
import com.example.news_backend.network.AuthenticationResponse
import com.example.news_backend.network.UserResponse
import com.example.news_backend.network.request.LoginRequest
import com.example.news_backend.network.request.SignupRequest
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Constants
import com.example.news_backend.utils.Resource
import retrofit2.Call

class AccountViewModel : BaseViewModelImpl() {
    private val _loginResult = MutableLiveData<Resource<ApiResponse<AuthenticationResponse>>>()
    val loginResult: LiveData<Resource<ApiResponse<AuthenticationResponse>>> = _loginResult

    private val _signupResult = MutableLiveData<Resource<ApiResponse<UserResponse>>>()
    val signupResult: LiveData<Resource<ApiResponse<UserResponse>>> = _signupResult

    val apiService = RetrofitBase.apiService(AppNewsService::class.java)

    fun login(username: String, password: String) {
        performAction(_loginResult) {
            createLoginCall(LoginRequest(username, password))
        }
    }

    fun signup(name: String, username: String, email: String, password: String, roles: Set<String> = setOf(Constants.ROLE_USER)) {
        performAction(_signupResult) {
            createSignupCall(SignupRequest(name, username, email, password, roles))
        }
    }

    private fun createLoginCall(request: LoginRequest): Call<ApiResponse<AuthenticationResponse>> {
        return apiService.login(request)
    }

    private fun createSignupCall(request: SignupRequest): Call<ApiResponse<UserResponse>> {
        return apiService.signup(request)
    }
}
