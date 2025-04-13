package com.example.news_backend.data.repository

import com.example.news_backend.data.source.api.RetrofitNews
import com.example.news_backend.data.source.db.ArticleDatabase
import com.example.news_backend.data.source.db.NewsAPI

class NewsRepository(
    val db: ArticleDatabase
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitNews.apiService(NewsAPI::class.java).getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitNews.apiService(NewsAPI::class.java).searchForNews(searchQuery, pageNumber)

}