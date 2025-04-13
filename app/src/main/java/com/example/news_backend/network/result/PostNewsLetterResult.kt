package com.example.news_backend.network.result

import com.example.news_backend.network.response.PostNewsLetterResponse

sealed class PostNewsLetterResult {
    data class Success(val postNewsLetterResponse: PostNewsLetterResponse) : PostNewsLetterResult()
    data class Error(val postNewsLetterResponse: PostNewsLetterResponse) : PostNewsLetterResult()
}