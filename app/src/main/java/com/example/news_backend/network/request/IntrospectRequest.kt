package com.example.news_backend.network.request

data class IntrospectRequest(
    var authenticated: Boolean,
    var token: String
)