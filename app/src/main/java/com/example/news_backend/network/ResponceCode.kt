package com.example.news_backend.network

import java.time.LocalDateTime

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)

data class UserResponse (
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val roles: Set<String>,
    val timestamps: Timestamps
)

data class Timestamps(
    val created_at: LocalDateTime,
    val updated_at: LocalDateTime
)

data class AuthenticationResponse(
    val authenticated: Boolean,
    val token: String,
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val roles: Set<String>
)

data class IntrospectResponse(
    val valid: Boolean
)

data class Error(
    val code: Int,
    val message: String
)