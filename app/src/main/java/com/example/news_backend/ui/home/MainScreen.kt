package com.example.news_backend.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
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
import com.example.news_backend.ui.bantin.BanTinItem
import com.example.news_backend.ui.bantin.BanTinViewModel
import com.example.news_backend.ui.bantin.HotNewsSection
import com.example.news_backend.ui.football.FootballSection
import com.example.news_backend.ui.football.FootballViewModel
import com.example.news_backend.ui.save.SaveBanTinViewModel
import com.example.news_backend.utils.Constants
import com.example.news_backend.utils.Resource

@Composable
fun MainScreen(navController: NavController,  viewModelview: FootballViewModel = viewModel(),) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val permission = remember { mutableStateOf<String?>(Constants.ROLE_ADMIN) }

    val colorScheme = MaterialTheme.colorScheme

    // Gọi lại API mỗi khi vào màn hình
    LaunchedEffect(Unit) {
        viewModelview.fetchDataCallAPI()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = colorScheme.surface
            ) {
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
            containerColor = colorScheme.background,
            content = { innerPadding ->
                val viewModel: BanTinViewModel = viewModel()
                val saveViewModel: SaveBanTinViewModel = viewModel()

                MainContent(
                    innerPadding = innerPadding,
                    navController = navController,
                    viewModel = viewModel,
                    saveViewModel = saveViewModel,
                    onOpenWebView = { link ->
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
    val colorScheme = MaterialTheme.colorScheme

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "News App",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onPrimaryContainer
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = null,
                    tint = colorScheme.onPrimaryContainer
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: Search */ }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = colorScheme.onPrimaryContainer
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorScheme.background,
            titleContentColor = colorScheme.onPrimaryContainer,
            navigationIconContentColor = colorScheme.onPrimaryContainer,
            actionIconContentColor = colorScheme.onPrimaryContainer
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

    val colorScheme = MaterialTheme.colorScheme
    val selectedColor = colorScheme.primary
    val unselectedColor = colorScheme.onSurfaceVariant
    val backgroundColor = colorScheme.surface

    Surface(
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                Column(
                    modifier = Modifier
                        .clickable {
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
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
                        tint = if (isSelected) Color(0xFF8A96EE)  else unselectedColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        color = if (isSelected) Color(0xFF8A96EE)  else unselectedColor
                    )
                }
            }
        }
    }
}


data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)
//  Bottom Home


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

    val colorScheme = MaterialTheme.colorScheme
    val backgroundColor = colorScheme.surface
    val contentColor = colorScheme.onSurface
    val selectedColor = colorScheme.primary
    val unselectedColor = colorScheme.onSurfaceVariant

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp,
        containerColor = backgroundColor,
        contentColor = contentColor,
        modifier = Modifier.fillMaxWidth(),
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = Color(0xFF4CAF50)
            )
        }
    ) {
        tabTitles.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index
            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        color = if (isSelected) Color(0xFF4CAF50) else unselectedColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
        }
    }
}
