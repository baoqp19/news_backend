package com.example.news_backend.ui.home

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.ui.Navbar.CategoryScreen
import com.example.news_backend.ui.Navbar.PostFoolBallScreen
import com.example.news_backend.ui.Navbar.PostNewsScreen
import com.example.news_backend.ui.Navbar.UserListScreen
import com.example.news_backend.ui.account.AccountViewModel
import com.example.news_backend.ui.account.screen.AccountScreen
import com.example.news_backend.ui.account.screen.LoginScreen
import com.example.news_backend.ui.account.screen.UpdateUserScreen
import com.example.news_backend.ui.bantin.BanTinItem
import com.example.news_backend.ui.bantin.BanTinScreen
import com.example.news_backend.ui.bantin.BanTinViewModel
import com.example.news_backend.ui.menu.CombinedMenuSettingsScreen
import com.example.news_backend.ui.menu.ThemeViewModel
import com.example.news_backend.ui.menu.WebViewScreen
import com.example.news_backend.ui.save.SaveBanTinScreen
import com.example.news_backend.ui.save.WebviewScreen
import com.example.news_backend.ui.search.SearchScreen
import com.example.news_backend.ui.search.news.SearchNewsViewModel

@Composable
fun AppNavigation(themeViewModel: ThemeViewModel) {
    // Khởi tạo NavController
    val navController = rememberNavController()

    val startDestination = if (DataLocalManager.getInstance().getInfoTokenKey().isNullOrEmpty()) {
        "login"
    } else {
        "home"
    }

    // Liên kết NavController với NavHost
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            val viewModel: AccountViewModel = viewModel()
            AccountScreen(navController = navController, viewModel = viewModel)
        }

        composable("home") {
            MainScreen(navController = navController)
        }
        composable("category"){
            CategoryScreen(
                onOpenWebView = { link ->
                    navController.navigate("webview?link=$link")
                }
            )
        }
        composable("search") {
            val viewModel: BanTinViewModel = viewModel() // Sử dụng viewModel của Navigation
            SearchScreen(
                navController = navController,
                viewModel = viewModel,
                onOpenWebView = { link ->
                    navController.navigate("webview?link=$link")
                }
                )
        }

        composable("save") {
            SaveBanTinScreen(
                navController = navController,
                onBack = { navController.popBackStack() },
                onOpenSetting = { navController.navigate("setting") },
                onOpenWebView = { link ->
                    navController.navigate("webview?link=$link")
                }
            )
        }
        composable(
            "webview?link={link}",
            arguments = listOf(navArgument("link") { defaultValue = "" })
        ) { backStackEntry ->
            val link = backStackEntry.arguments?.getString("link") ?: ""
            WebviewScreen(link = link)
        }
        composable("profile"){
            val viewModel: BanTinViewModel = viewModel()
            CombinedMenuSettingsScreen(
                navController = navController,
                themeViewModel = themeViewModel
            )
        }
        composable("userinfo"){
            val viewModel: BanTinViewModel = viewModel()
            UpdateUserScreen(navController = navController)
        }


        composable(
            "webview?url={url}&title={title}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            WebViewScreen(url = url, title = title) {
                navController.popBackStack()
            }
        }
        composable("categoryNav") {
            CategoryScreen(
                onOpenWebView = { link ->
                    navController.navigate("webview?link=$link")
                }
            )
        }
        composable("postNews") {
            PostNewsScreen(
                onSubmitSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable("postFoolball") {
            PostFoolBallScreen(
                onSubmitSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable("userlist") { UserListScreen() }

    }
}