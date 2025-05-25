package com.example.news_backend.ui.save


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.news_backend.utils.Resource
import com.example.news_backend.R
import com.example.news_backend.data.models.BanTin
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.network.response.SavePosResponse
import com.example.news_backend.ui.home.BottomNavigationBar


@Composable
fun SaveBanTinScreen(
    navController: NavController,
    viewModel: SaveBanTinViewModel = viewModel(),
    onBack: () -> Unit,
    onOpenSetting: () -> Unit,
    onOpenWebView: (String) -> Unit
) {
    val context = LocalContext.current
    val banTinState by viewModel.getSaveBanTin.observeAsState()
    val closeParam = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getListAllNewsSave()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController)},
        containerColor = Color.Black // Màu nền cho Scaffold
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.Black)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = {
                    if (closeParam.value) onOpenSetting() else onBack()
                }) {
                    Icon(
                        painter = painterResource(
                            id = if (closeParam.value)
                                R.drawable.ic_setting
                            else
                                R.drawable.ic_arrow_back_24px
                        ),
                        tint = Color.Unspecified,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = "Tin Đã Đọc",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(onClick = {
                    viewModel.deleteAllListNewsSave()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        tint = Color.Unspecified,
                        contentDescription = "Delete",
                    )
                }
            }

            banTinState?.let { result ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    when (result) {
                        is Resource.Success<*> -> {
                            val data = result.data ?: emptyList()
                            items(data) { item ->
                                BanTinItem(
                                    item = item,
                                    onClick = { link ->
                                        onOpenWebView(link)
                                    },
                                    onSave = { newsItem ->
                                        // Khi người dùng click vào, gọi hàm lưu tin vào backend
                                        val userId = DataLocalManager.getInstance().getInfoUserId()
                                        viewModel.postNewsSave(
                                            title = newsItem.title,
                                            link = newsItem.link,
                                            img = newsItem.img,
                                            pubDate = newsItem.pubDate,
                                            userId = userId
                                        )
                                    }
                                )
                            }
                        }

                        is Resource.Loading<*> -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        is Resource.Error<*> -> {
                            item {
                                Text(
                                    "Lỗi khi tải tin tức",
                                    color = Color.Red,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BanTinItem(
    item: SavePosResponse,
    onClick: (String) -> Unit, // Thêm hàm onClick để nhận URL và mở WebView
    onSave: (SavePosResponse) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), // Màu nền tối
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = item.img.ifEmpty { "https://via.placeholder.com/150" },
                contentDescription = "Ảnh bản tin",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        onSave(item)
                        onClick(item.link)
                    },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Clock",
                        tint = Color(0xFFFFC107), // màu vàng như hình
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = item.pubDate,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "VnExpress",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun WebviewScreen(link: String) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl(link)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}