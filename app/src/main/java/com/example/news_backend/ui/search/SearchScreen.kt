package com.example.news_backend.ui.search

import androidx.compose.runtime.Composable
import com.example.news_backend.ui.home.BottomNavigationBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.news_backend.data.models.BanTin
import com.example.news_backend.network.response.SavePosResponse
import com.example.news_backend.ui.bantin.BanTinViewModel
import com.example.news_backend.utils.Constants
import com.example.news_backend.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: BanTinViewModel,
    onOpenWebView: (String) -> Unit
) {
    var isSearching by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    val banTinState by viewModel.listTinTuc.observeAsState(Resource.Loading())
    val allBanTin = remember { mutableStateListOf<BanTin>() }

    val filteredBanTin = remember(query, allBanTin) {
        if (query.isBlank()) allBanTin
        else allBanTin.filter { it.title?.contains(query, ignoreCase = true) == true }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchDataCallAPI(Constants.full)
    }

    LaunchedEffect(banTinState) {
        if (banTinState is Resource.Success) {
            val list = banTinState.data?.data.orEmpty()
            allBanTin.clear()
            allBanTin.addAll(list)
        }
    }

    Scaffold(
        topBar = {
            SearchAppBar(
                query = query,
                onQueryChange = { query = it },
                onSearchToggle = { isSearching = !isSearching },
                isSearching = isSearching,
                onClearSearch = { query = "" },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF497BC4),
                    unfocusedBorderColor = Color(0xFF32578D).copy(alpha = 0.5f),
                    focusedLeadingIconColor = Color(0xFFC8D2E0),
                    unfocusedLeadingIconColor = Color(0xFFBEC5D2).copy(alpha = 0.5f),
                    focusedTextColor = Color(0xFFA0AEBE),
                    unfocusedTextColor = Color(0xFFA3AEC0).copy(alpha = 0.8f)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
                containerColor = Color(0xFF121212),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (val result = banTinState) {
                is Resource.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is Resource.Error -> Text(
                    text = "Lỗi: ${result.message}",
                    color = MaterialTheme.colorScheme.error
                )

                is Resource.Success -> {
                    if (filteredBanTin.isEmpty()) {
                        Text("Không tìm thấy bản tin nào.")
                    } else {
                        LazyColumn {
                            items(filteredBanTin) { tinTuc ->
                                BanTinItem(tinTuc = tinTuc, onClick = {
                                        onOpenWebView(tinTuc.link)
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    isSearching: Boolean,
    onClearSearch: () -> Unit,
    colors: TextFieldColors
) {
    CenterAlignedTopAppBar(
        title = {
            if (isSearching) {
                SearchBarTextField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onClearSearch = onClearSearch,
                    colors = colors
                )
            } else {
                Text(
                    text = "Tìm kiếm bản tin",
                    color = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchToggle) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black
        )
    )
}



@Composable
fun SearchBarTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    colors: TextFieldColors,
    iconTint: Color = MaterialTheme.colorScheme.primary
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Tìm kiếm...") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearSearch) {
                    Icon(Icons.Default.Close, contentDescription = "Xoá", tint = iconTint)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = colors
    )
}


@Composable
fun BanTinItem(
    tinTuc: BanTin,
    onClick: (String) -> Unit // Thêm hàm onClick để nhận URL và mở WebView
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()  // Đảm bảo Card chiếm hết chiều rộng
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
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick(tinTuc.link) }, // Xử lý sự kiện nhấn vào ảnh
                contentScale = ContentScale.Crop,
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
                        tint = Color(0xFFFFC107), // Màu vàng như hình
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