package com.example.news_backend.network.request

data class PostNewsLetterRequest(
    val title: String,
    val link: String,
    val img: String,
    val pubDate: String,
    val category: String
)