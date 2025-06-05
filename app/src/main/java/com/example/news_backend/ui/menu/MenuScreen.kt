package com.example.news_backend.ui.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.annotation.DrawableRes
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.news_backend.R
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.ui.Navbar.DrawerItem
import com.example.news_backend.ui.home.BottomNavigationBar
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun CombinedMenuSettingsScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    val name = remember { DataLocalManager.getInstance().getInfoUserName() ?: "Người dùng" }
    val email = remember { DataLocalManager.getInstance().getInfoEmail() ?: "email@example.com" }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                SettingsDialogContent(
                    navController = navController,
                    onChangePassword = { showDialog = false },
                    onUpdateInfo = {
                        showDialog = false
                        navController.navigate("userinfo")
                    },
                    onDeleteInfo = { showDialog = false }
                )
            }
        }

        val danhMucItems = listOf(
            R.drawable.ic_calender to "Lịch thi đấu",
            R.drawable.ic_weather to "Thời tiết",
            R.drawable.ic_ball to "BXH, kết quả"
        )

        val danhMucLinks = mapOf(
            "Lịch thi đấu" to "https://vnexpress.net/the-thao/du-lieu-bong-da",
            "Thời tiết" to "https://vnexpress.net/thoi-tiet/da-nang",
            "BXH, kết quả" to "https://vnexpress.net/bong-da"
        )

        val tienIchItems = listOf(
            R.drawable.ic_language to "Ngôn ngữ",
            R.drawable.ic_o_time to "Chế độ tối",
            R.drawable.ic_calender to "Lịch",
            R.drawable.ic_weather to "Thời tiết",
            R.drawable.ic_setting to "Cài đặt"
        )

        val khacItems = listOf(
            R.drawable.ic_phone to "Phản hồi",
            R.drawable.ic_ball to "Các điều khoản",
            R.drawable.ic_logout to "Đăng xuất"
        )

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Cài đặt",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFFFF6F61))
                            .clickable { }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "0828-7853",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDialog = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.userlogin),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .padding(8.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(name, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            Text(email, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Các Trang Mạng Xã Hội", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.facebook), null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pqbao.05"))
                                        context.startActivity(intent)
                                    },
                                tint = Color(0xFF1877F2)
                            )
                            Icon(painter = painterResource(id = R.drawable.logo_messenger_new), contentDescription = null, modifier = Modifier.size(32.dp), tint = Color.Unspecified)
                            Icon(painter = painterResource(id = R.drawable.zalo_sharelogo), null, modifier = Modifier.size(32.dp), tint = Color.Unspecified)
                            Icon(
                                painter = painterResource(id = R.drawable.ic_github), null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/baoqp19"))
                                        context.startActivity(intent)
                                    },
                                tint = Color(0xFF1877F2)
                            )
                            Spacer(Modifier.weight(1f))
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            item { SectionTitle("Danh mục") }
            items(danhMucItems) { (icon, title) ->
                MenuCardItem(iconRes = icon, title = title) {
                    val url = danhMucLinks[title] ?: return@MenuCardItem
                    val encodedUrl = URLEncoder.encode(url, "UTF-8")
                    val encodedTitle = URLEncoder.encode(title, "UTF-8")
                    navController.navigate("webview?url=$encodedUrl&title=$encodedTitle")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item { SectionTitle("Tiện ích & cài đặt") }
            items(tienIchItems) { (icon, title) ->
                when (title) {
                    "Chế độ tối" -> {
                        DarkModeSwitchCard(
                            iconRes = R.drawable.ic_o_time,
                            title = title,
                            isDarkMode = themeViewModel.isDarkTheme.value,
                            onToggle = { themeViewModel.toggleTheme() }
                        )
                    }
                    "Lịch" -> {
                        SettingsCardItem(iconRes = icon, title = title) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.24h.com.vn/lich-van-nien-c936.html"))
                            context.startActivity(intent)
                        }
                    }
                    else -> {
                        SettingsCardItem(iconRes = icon, title = title) {
                            // Xử lý click khác
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item { SectionTitle("Khác") }
            items(khacItems) { (icon, title) ->
                SettingsCardItem(iconRes = icon, title = title) {
                    if (title == "Đăng xuất") {
                            DataLocalManager.getInstance().removeValueFromSharedPreferences()
                            scope.launch {
                                drawerState.close()
                                navController.navigate("login") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true // Xoá toàn bộ stack cũ
                                    }
                                    launchSingleTop = true
                                }
                            }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


class ThemeViewModel : ViewModel() {
    var isDarkTheme = mutableStateOf(false)
        private set

    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }
}


@Composable
fun DarkModeSwitchCard(
    iconRes: Int,
    title: String,
    isDarkMode: Boolean,
    onToggle: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Sắp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            // Spacer để tạo khoảng cách giữa phần tử Icon-Text và Switch
            Spacer(modifier = Modifier.weight(1f))
            // Switch bật/tắt theme
            Switch(
                checked = isDarkMode,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.onBackground
    )
}


@Composable
fun SettingsCardItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun MenuCardItem(
    @DrawableRes iconRes: Int,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// LỊCH THI ĐẤU
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(url: String, title: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

















