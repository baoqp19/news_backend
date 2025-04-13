package com.example.news_backend.ui.base

interface BasePersenter {
    fun showLoading()
    fun hideLoading()
    fun responseError(error: String)
}