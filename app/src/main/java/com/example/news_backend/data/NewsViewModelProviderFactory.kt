package com.example.news_backend.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news_backend.data.repository.NewsRepository
import com.example.news_backend.ui.search.news.SearchNewsViewModel

class NewsViewModelProviderFactory(
    val newsRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchNewsViewModel(newsRepository) as T
    }
}