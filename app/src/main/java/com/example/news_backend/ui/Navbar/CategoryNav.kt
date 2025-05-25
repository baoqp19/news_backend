package com.example.news_backend.ui.Navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Tab
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news_backend.ui.bantin.BanTinViewModel
import com.example.news_backend.utils.Resource
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.graphics.Color
import com.example.news_backend.ui.bantin.BanTinItem




@Composable
fun CategoryScreen(
    viewModel: BanTinViewModel = viewModel(),
    onOpenWebView: (String) -> Unit
) {
    val tabTitles = listOf(
        "PHỔ BIẾN", "NỔI BẬT", "MỚI NHẤT", "THẾ GIỚI", "THỂ THAO",
        "PHÁP LUẬT", "GIÁO DỤC", "SỨC KHỎE", "ĐỜI SỐNG", "KHOA HỌC",
        "KINH DOANH", "TÂM SỰ", "SỐ HÓA", "DU LỊCH"
    )

    // Khóa thật để gọi API (phải khớp theo backend)
    val tabKeys = listOf(
        "tin-moi-nhat", "tin-noi-bat", "tin-moi-nhat", "tin-the-gioi", "tin-the-thao",
        "tin-phap-luat", "tin-giao-duc", "tin-suc-khoe", "tin-doi-song", "tin-khoa-hoc",
        "tin-kinh-doanh", "tin-tam-su", "tin-so-hoa", "tin-du-lich"
    )

    val selectedTab = remember { mutableIntStateOf(0) }
    val listTinTuc by viewModel.listTinTuc.observeAsState()

    // Gọi API khi tab được chọn
    LaunchedEffect(selectedTab.intValue) {
        val selectedKey = tabKeys[selectedTab.intValue]
        viewModel.fetchDataCallAPI(selectedKey)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // nền tối
    ) {
        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab.intValue,
            edgePadding = 12.dp,
            containerColor = Color(0xFF1E1E1E),
            contentColor = Color.White,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab.intValue])
                        .height(3.dp)
                        .background(Color(0xFF4CAF50), RoundedCornerShape(2.dp))
                )
            },
            divider = {}
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab.intValue == index,
                    onClick = { selectedTab.intValue = index },
                    selectedContentColor = Color(0xFF4CAF50),
                    unselectedContentColor = Color.White.copy(alpha = 0.6f),
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab.intValue == index) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                )
            }
        }
// Nội dung
        when (val resource = listTinTuc) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4CAF50))
                }
            }

            is Resource.Success -> {
                // Sắp xếp theo id giảm dần ngay tại đây
                val newsList = resource.data?.data.orEmpty().sortedByDescending { it.title }
                if (newsList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Không có dữ liệu",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(newsList) { _, tinTuc ->
                            BanTinItem(
                                tinTuc = tinTuc,
                                onClick = {
                                    tinTuc.link?.let { onOpenWebView(it) }
                                }
                            )
                        }
                    }
                }
            }

            is Resource.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Đã xảy ra lỗi: ${resource.message ?: "Không rõ lỗi"}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Chưa có dữ liệu",
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }

    }

}