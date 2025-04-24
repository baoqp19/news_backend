package com.example.news_backend.ui.bantin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import coil.compose.AsyncImage

import androidx.compose.material.icons.Icons

import androidx.navigation.NavController

import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Lock

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import com.example.news_backend.data.models.BanTin
import com.example.news_backend.ui.home.BottomNavigationBar
import com.example.news_backend.utils.Resource


@Composable
fun BanTinScreen(
    navController: NavController,
    viewModel: BanTinViewModel,
    onOpenWebView: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.fetchDataCallAPI("full")
    }

    val resource by viewModel.listTinTuc.observeAsState(Resource.Loading())

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        containerColor = Color.Black // Màu nền cho Scaffold
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = resource) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    val banTinList = state.data?.data.orEmpty()
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(banTinList) { _, tinTuc ->
                            BanTinItem(tinTuc = tinTuc, onClick = {
                                onOpenWebView(tinTuc.link)
                            })
                        }
                    }
                }

                is Resource.Error -> {
                    Text(
                        text = "Lỗi: ${state.message ?: "Không xác định"}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BanTinItem(
    tinTuc: BanTin,
    onClick: (String) -> Unit
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
                model = tinTuc.img.ifEmpty { "https://via.placeholder.com/150" },
                contentDescription = "Ảnh bản tin",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = tinTuc.title,
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
                        text = tinTuc.pubDate,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "VnExpress",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    )
                }
            }
        }
    }
}