package com.example.news_backend.network.request

data class UpdateUserRequest(
    val name: String,
    val email: String,
    val password: String
)