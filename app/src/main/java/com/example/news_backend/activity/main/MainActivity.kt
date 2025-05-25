package com.example.news_backend.activity.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news_backend.ui.home.AppNavigation
import com.example.news_backend.ui.home.AppTheme
import com.example.news_backend.ui.menu.ThemeViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()

            AppTheme(isDarkTheme = themeViewModel.isDarkTheme.value) {
                AppNavigation(themeViewModel = themeViewModel)
            }
        }
    }
}
