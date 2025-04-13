package com.example.news_backend.network.response

data class PostNewsLetterResponse(
    val title: String,
    val link: String,
    val img: String,
    val pubDate: String
)