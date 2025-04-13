package com.example.news_backend.network.request

data class ChangePasswordRequest(
    val name: String,
    val email: String,
    val password: String
)