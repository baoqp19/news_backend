package com.example.news_backend.network.request

 data class FoolBallRequest (
    var title: String,
    var thumbnail: String,
    var url: String,
    var date: String,
 )

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)

data class FootballDto(
    val title: String,
    val thumbnail: String,
    val url: String,
    val date: String
)