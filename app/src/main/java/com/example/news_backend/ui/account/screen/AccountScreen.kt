package com.example.news_backend.ui.account.screen

import SignupScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.news_backend.ui.account.AccountViewModel

@Composable
fun AccountScreen(navController: NavHostController = rememberNavController(), viewModel: AccountViewModel = viewModel()) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Đăng nhập", "Đăng ký")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        // Khi tab thay đổi, hiển thị màn hình Đăng nhập hoặc Đăng ký tương ứng
        when (selectedTabIndex) {
            0 -> LoginScreen(navController, viewModel)  // Đăng nhập
            1 -> SignupScreen(navController, viewModel) // Đăng ký
        }
    }
}