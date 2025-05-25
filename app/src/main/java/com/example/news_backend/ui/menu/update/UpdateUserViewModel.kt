package com.example.news_backend.ui.menu.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.data.source.api.AppNewsService
import com.example.news_backend.data.source.api.RetrofitBase
import com.example.news_backend.network.ApiResponse
import com.example.news_backend.network.UserResponse
import com.example.news_backend.network.request.UpdateUserRequest
import com.example.news_backend.network.response.User
import com.example.news_backend.network.response.UserUpdateRoleRequest
import com.example.news_backend.ui.base.BaseViewModelImpl
import com.example.news_backend.utils.Resource
import kotlinx.coroutines.launch

class UpdateUserViewModel : BaseViewModelImpl() {
    private val _updateUser = MutableLiveData<Resource<ApiResponse<UserResponse>>>()
    val updateUser: LiveData<Resource<ApiResponse<UserResponse>>> = _updateUser

    val apiService = RetrofitBase.apiService(AppNewsService::class.java)

    val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJob2FuZ3RpZW4yazMuY29tIiwic3ViIjoicXVvY2Jhb2R0cjcwQGdtYWlsLmNvbSIsImV4cCI6MTc1MDYwMzg5MCwiaWF0IjoxNzQ4MDExODkwLCJzY29wZSI6IkFETUlOIn0.FB9fWpptnf2LPxXyMBNuzCSsEL05H2V7ZjW0JFq18mZGN2PJ2pbDwPZlbVmVstWvzlsQJ5Mg_pUORVivRRdWPA"

    fun updateUserInfo(name: String, email: String, password: String) {
        val updateUserRequest = UpdateUserRequest(name, email, password)
        val userId = DataLocalManager.getInstance().getInfoUserId()
        performAction(_updateUser) {
            apiService.updateUser(userId, updateUserRequest, token)
        }
    }

    private val _allUsers = MutableLiveData<Resource<List<User>>>()
    val allUsers: LiveData<Resource<List<User>>> = _allUsers

    fun fetchAllUsers() {
        viewModelScope.launch {
            _allUsers.value = Resource.Loading()
            try {
                val response = apiService.getAllUsers(token)
                _allUsers.value = Resource.Success(response.data)  // Lấy danh sách user từ data
            } catch (e: Exception) {
                _allUsers.value = Resource.Error("Lỗi: ${e.message}")
            }
        }
    }

    private val _updateRoleResult = MutableLiveData<Resource<String>>()
    val updateRoleResult: LiveData<Resource<String>> = _updateRoleResult

    fun updateUserRole(userId: Long, newRole: String) {
        viewModelScope.launch {
            _updateRoleResult.value = Resource.Loading()
            try {
                val response = apiService.updateUserRole(
                    userId = userId,
                    request = UserUpdateRoleRequest(newRole),
                    token = token
                )
                if (response.isSuccessful) {
                    _updateRoleResult.value = Resource.Success("Cập nhật thành công")
                } else {
                    _updateRoleResult.value = Resource.Error("Lỗi: ${response.code()}")
                }
            } catch (e: Exception) {
                _updateRoleResult.value = Resource.Error("Lỗi: ${e.message}")
            }
        }
    }
}