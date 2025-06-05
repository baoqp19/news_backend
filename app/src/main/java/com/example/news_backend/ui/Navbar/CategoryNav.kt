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
    val colorScheme = MaterialTheme.colorScheme

    val tabTitles = listOf(
        "PHỔ BIẾN", "NỔI BẬT", "MỚI NHẤT", "THẾ GIỚI", "THỂ THAO",
        "PHÁP LUẬT", "GIÁO DỤC", "SỨC KHỎE", "ĐỜI SỐNG", "KHOA HỌC",
        "KINH DOANH", "TÂM SỰ", "SỐ HÓA", "DU LỊCH"
    )

    val tabKeys = listOf(
        "tin-moi-nhat", "tin-noi-bat", "tin-moi-nhat", "tin-the-gioi", "tin-the-thao",
        "tin-phap-luat", "tin-giao-duc", "tin-suc-khoe", "tin-doi-song", "tin-khoa-hoc",
        "tin-kinh-doanh", "tin-tam-su", "tin-so-hoa", "tin-du-lich"
    )

    val selectedTab = remember { mutableIntStateOf(0) }
    val listTinTuc by viewModel.listTinTuc.observeAsState()

    LaunchedEffect(selectedTab.intValue) {
        val selectedKey = tabKeys[selectedTab.intValue]
        viewModel.fetchDataCallAPI(selectedKey)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab.intValue,
            edgePadding = 12.dp,
            containerColor = colorScheme.background,
            contentColor = colorScheme.onSurface,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab.intValue])
                        .height(3.dp)
                        .background(Color(0xFF4CAF50))
                        )
            },
            divider = {}
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab.intValue == index,
                    onClick = { selectedTab.intValue = index },
                    selectedContentColor = Color(0xFF4CAF50),
                    unselectedContentColor = colorScheme.onSurface.copy(alpha = 0.6f),
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
                val newsList = resource.data?.data.orEmpty().sortedByDescending { it.title }
                if (newsList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Không có dữ liệu",
                            color = colorScheme.onBackground.copy(alpha = 0.8f)
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
                        color = colorScheme.error,
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
                        color = colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
