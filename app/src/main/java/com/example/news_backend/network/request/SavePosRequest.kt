package com.example.news_backend.network.request

data class SavePosRequest(
    val title: String,
    val link: String,
    val img: String,
    val pubDate: String,
    val userId: Long
)
