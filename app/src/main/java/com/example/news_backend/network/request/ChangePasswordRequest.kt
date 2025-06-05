package com.example.news_backend.network.request

data class ChangePasswordRequest(
    val name: String,
    val email: String,
    val password: String
)

data class ChangePassword(
    val id: Long,
    val password: String
)

data class ChangePasswordResponse(
    val message: String // hoặc bất kỳ trường nào bạn mong đợi
)