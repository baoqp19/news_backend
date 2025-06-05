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

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            SearchAppBar(
                query = query,
                onQueryChange = { query = it },
                onSearchToggle = { isSearching = !isSearching },
                isSearching = isSearching,
                onClearSearch = { query = "" },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedLeadingIconColor = colorScheme.primary,
                    unfocusedLeadingIconColor = colorScheme.onSurfaceVariant,
                    focusedTextColor = colorScheme.onSurface,
                    unfocusedTextColor = colorScheme.onSurfaceVariant
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = colorScheme.background
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
                    CircularProgressIndicator(color = colorScheme.primary)
                }

                is Resource.Error -> Text(
                    text = "Lỗi: ${result.message}",
                    color = colorScheme.error
                )

                is Resource.Success -> {
                    if (filteredBanTin.isEmpty()) {
                        Text(
                            text = "Không tìm thấy bản tin nào.",
                            color = colorScheme.onBackground
                        )
                    } else {
                        LazyColumn {
                            items(filteredBanTin) { tinTuc ->
                                BanTinItem(
                                    tinTuc = tinTuc,
                                    onClick = { onOpenWebView(tinTuc.link) }
                                )
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
    val colorScheme = MaterialTheme.colorScheme

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
                    color = colorScheme.onPrimaryContainer
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchToggle) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    tint = colorScheme.onPrimaryContainer
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorScheme.background
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
        placeholder = {
            Text("Tìm kiếm...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
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
    onClick: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
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
                    .clickable { onClick(tinTuc.link) },
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
                        color = colorScheme.onSurface
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Clock",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = tinTuc.pubDate,
                        style = MaterialTheme.typography.bodySmall.copy(color = colorScheme.onSurface)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "VnExpress",
                        fontSize = 12.sp,
                        color = colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
