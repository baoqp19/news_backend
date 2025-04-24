package com.example.news_backend.activity.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news_backend.R
import com.example.news_backend.databinding.ActivityMainBinding
import com.example.news_backend.data.source.db.ArticleDatabase
import com.example.news_backend.ui.menu.MenuFragment
import com.example.news_backend.data.repository.NewsRepository
import com.example.news_backend.ui.search.news.SearchNewsViewModel
import com.example.news_backend.data.NewsViewModelProviderFactory
import com.example.news_backend.ui.bantin.BanTinFragment
import com.example.news_backend.ui.home.AppNavigation
import com.example.news_backend.ui.home.AppTheme
import com.example.news_backend.ui.menu.ThemeViewModel
import com.example.news_backend.ui.save.SaveBanTinFragment
import com.example.news_backend.ui.search.bantin.SearchBanTinFragment
import com.example.news_backend.utils.Constants

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
