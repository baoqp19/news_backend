package com.example.news_backend.ui.Navbar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.news_backend.R
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.utils.Constants
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState, permission: String?) {
    val showUserDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val userInfo = remember { DataLocalManager.getInstance() }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFC107)) // Màu vàng
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.post), // icon header
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Một Số Tuỳ Chọn",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "giúp truy cập nhanh những tiện ích",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Các mục trong Drawer
        DrawerItem(
            title = "Trang chủ",
            icon = Icons.Default.Home
        ) {
            scope.launch { drawerState.close() }
        }

        DrawerItem(
            title = "Danh mục",
            icon = Icons.Default.List
        ) {
            navController.navigate("categoryNav")
            scope.launch { drawerState.close() }
        }

        // Chỉ hiện các mục này nếu là ADMIN
        if (permission == userInfo.getInfoUserRole()) {
            DrawerItem(
                title = "Đăng tin tức",
                icon = Icons.Default.Send
            ) {
                navController.navigate("postNews")
                scope.launch { drawerState.close() }
            }

            DrawerItem(
                title = "Đăng tin bóng dá",
                icon = Icons.Default.AddCircle
            ) {
                navController.navigate("postFoolball")
                scope.launch { drawerState.close() }
            }

            DrawerItem(
                title = "Phê duyệt tin tức",
                icon = Icons.Default.KeyboardArrowDown
            ) {
                scope.launch { drawerState.close() }
            }

            DrawerItem(
                title = "Phân quyền",
                icon = Icons.Default.Person
            ) {
                scope.launch {
                    drawerState.close()
                    navController.navigate("userlist")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tiêu đề phụ
        Text(
            text = "Tài khoản",
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.labelLarge.copy(color = Color.Gray)
        )

        DrawerItem(
            title = "Thông tin tài khoản",
            icon = Icons.Default.Person
        ) {
            showUserDialog.value = true
            scope.launch { drawerState.close() }
        }

        DrawerItem(
            title = "Đăng xuất",
            icon = Icons.Default.Lock
        ) {
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
    // Dialog hiển thị thông tin người dùng
    if (showUserDialog.value) {
        UserInfoDialog(onDismiss = { showUserDialog.value = false })
    }
}

@Composable
fun DrawerItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF1A237E)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

