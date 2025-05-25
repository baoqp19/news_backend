package com.example.news_backend.network.response

import com.google.gson.annotations.SerializedName

    data class UserResponseCoppy(
        val code: Int,
        val message: String,
        val data: List<User>
    )

    data class User(
        val id: Int,
        val name: String,
        val username: String,
        val email: String,
        val roles: List<String>,
        val timestamps: Timestamps
    )

    data class Timestamps(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )

    data class UserUpdateRoleRequest(
        val role: String
    )
