package com.example.news_backend.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import com.example.news_backend.data.models.BanTin
import com.example.news_backend.data.models.Football
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.ui.Navbar.DrawerContent
import com.example.news_backend.ui.bantin.BanTinViewModel
import com.example.news_backend.ui.football.FootballViewModel
import com.example.news_backend.ui.save.SaveBanTinViewModel
import com.example.news_backend.utils.Constants
import com.example.news_backend.utils.Resource


@Composable
fun MainScreen(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val permission = remember { mutableStateOf<String?>(Constants.ROLE_ADMIN) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(navController, drawerState, permission.value)
            }
        }
    ) {
        Scaffold(
            topBar = {
                NewsTopBar {
                    scope.launch { drawerState.open() }
                }
            },
            bottomBar = {
                BottomNavigationBar(navController)
            },
            containerColor = Color(0xFF121212),
            content = { innerPadding ->
                val viewModel: BanTinViewModel = viewModel()
                val saveViewModel: SaveBanTinViewModel = viewModel()
                MainContent(
                    innerPadding = innerPadding,
                    navController = navController,
                    viewModel = viewModel,
                    saveViewModel = saveViewModel,
                    onOpenWebView = { link ->
                        // Ví dụ: chuyển đến WebViewScreen và truyền URL
                        navController.navigate("webview?link=$link")
                    }
                )
            }
        )
    }
}

@Composable
fun MainContent(
    innerPadding: PaddingValues,
    navController: NavController,
    viewModel: BanTinViewModel = viewModel(),
    saveViewModel: SaveBanTinViewModel = viewModel(),
    onOpenWebView: (String) -> Unit
) {
    val selectedTab = remember { mutableIntStateOf(0) }
    val tabTitles = listOf(
        "tin-noi-bat",
        "tin-moi-nhat",
        "tin-the-gioi",
        "tin-the-thao",
        "tin-phap-luat",
        "tin-giao-duc",
        "tin-suc-khoe",
        "tin-doi-song",
        "tin-khoa-hoc",
        "tin-kinh-doanh",
        "tin-tam-su",
        "tin-so-hoa",
        "tin-du-lich",
        "full",
    )

    val listTinTuc by viewModel.listTinTuc.observeAsState()

    // Gọi API khi tab được chọn (khác 0)
    LaunchedEffect(selectedTab.intValue) {
        if (selectedTab.intValue > 0) {
            val selectedCategory = tabTitles[selectedTab.intValue]
            viewModel.fetchDataCallAPI(selectedCategory)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        // Tabs
        CategoryTabRow(
            selectedTabIndex = selectedTab.intValue,
            onTabSelected = { selectedTab.intValue = it }
        )

        Spacer(modifier = Modifier.height(8.dp))
        if (selectedTab.intValue == 0) {
            FootballSection( navController = navController,
                onOpenWebView = { link ->
                    navController.navigate("webview?link=$link")
                })
            Spacer(modifier = Modifier.height(8.dp))
            HotNewsSection(
                navController = navController,
                onOpenWebView = { link ->
                    navController.navigate("webview?link=$link")
                }
            )
        } else {
            listTinTuc?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is Resource.Success -> {
                        val data = resource.data?.data ?: emptyList()
                        LazyColumn {
                            items(data) { tinTuc ->
                                BanTinItem(
                                    tinTuc = tinTuc,
                                    onClick = { link ->
                                        onOpenWebView(link)
                                    },
                                    onSave = { newsItem ->
                                        val userId = DataLocalManager.getInstance().getInfoUserId()
                                        saveViewModel.postNewsSave(
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
                    }
                    is Resource.Error -> {
                        Text(
                            text = resource.message ?: "Có lỗi xảy ra!",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar(onMenuClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "News App",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: Search */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF1C1B1F),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("category", Icons.Default.ShoppingCart, "Category"),
        BottomNavItem("save", Icons.Default.AccountCircle, "Save"),
        BottomNavItem("search", Icons.Default.Search, "Search"),
        BottomNavItem("profile", Icons.Default.Person, "Profile")
    )

    Surface(
        color = Color(0xFF1C1B1F),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // 👉 CHỈ 50dp
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                Column(
                    modifier = Modifier
                        .clickable {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) Color(0xFF8A96EE) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        color = if (currentRoute == item.route) Color(0xFF8A96EE) else Color.Gray
                    )
                }
            }
        }
    }
}


data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)
//  Bottom Home

@Composable
fun FootballSection(
    navController: NavController,
    viewModel: FootballViewModel = viewModel(),
    onOpenWebView: (String) -> Unit
    ) {
    val state by viewModel.footballNews.observeAsState()

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Tin bóng đá",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text("Xem thêm", color = Color.Yellow, fontSize = 14.sp)
        }

        when (val result = state) {
            is Resource.Success<*> -> {
                val list = result.data?.data ?: emptyList()
                LazyRow(modifier = Modifier.padding(8.dp)) {
                    items(list) { football ->
                        FootballCard(football = football, onClick = {
                            onOpenWebView(football.url)
                        })
                    }
                }
            }

            is Resource.Loading<*> -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            is Resource.Error<*> -> Text("Lỗi khi tải tin bóng đá")
            else -> {}
        }
    }
}

@Composable
fun FootballCard(football: Football, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .clickable { onClick(football.url) },
    ) {
        AsyncImage(
            model = football.thumbnail,
            contentDescription = football.title,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            football.title,
            maxLines = 2, overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}


@Composable
fun HotNewsSection(
    navController: NavController,
    viewModel: BanTinViewModel = viewModel(),
    saveViewModel: SaveBanTinViewModel = viewModel(),
    onOpenWebView: (String) -> Unit
) {
    val state by viewModel.listTinTuc.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDataCallAPI(Constants.full)
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tin Nóng",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = "Xem thêm",
                color = Color.Yellow,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    // Có thể thêm hành động "Xem thêm" tại đây nếu cần
                }
            )
        }

        when (val result = state) {
            is Resource.Success<*> -> {
                val list = result.data?.data ?: emptyList()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 600.dp)
                ) {
                    items(list) { tinTuc ->
                        BanTinItem(
                            tinTuc = tinTuc,
                            onClick = { link ->
                                onOpenWebView(link)
                            },
                            onSave = { newsItem ->
                                val userId = DataLocalManager.getInstance().getInfoUserId()
                                saveViewModel.postNewsSave(
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
            }

            is Resource.Loading<*> -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Error<*> -> {
                Text("Lỗi khi tải tin nóng", color = Color.Red)
            }

            else -> Unit
        }
    }
}



@Composable
fun BanTinItem(
    tinTuc: BanTin,
    onClick: (String) -> Unit,
    onSave: (BanTin) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(tinTuc.img)
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        onSave(tinTuc)
                        onClick(tinTuc.link)
                    },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = tinTuc.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "time",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = tinTuc.pubDate,
                        fontSize = 12.sp,
                        color = Color(0xFFAAAAAA)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "VnExpress",
                        fontSize = 12.sp,
                        color = Color(0xFFBBBBBB),
                        modifier = Modifier.align(Alignment.Bottom)
                    )
                }
            }
        }
    }
}


@Composable
fun CategoryTabRow(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabTitles = listOf(
        "PHỔ BIẾN", "NỔI BẬT", "MỚI NHẤT", "THẾ GIỚI", "THỂ THAO",
        "PHÁP LUẬT", "GIÁO DỤC", "SỨC KHỎE", "ĐỜI SỐNG", "KHOA HỌC",
        "KINH DOANH", "TÂM SỰ", "SỐ HÓA", "DU LỊCH"
    )

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp,
        containerColor = Color.Black,
        contentColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = Color(0xFF4CAF50)
            )
        }
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                },
                text = {
                    Text(
                        text = title,
                        color = if (selectedTabIndex == index) Color(0xFF4CAF50) else Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
        }
    }
}