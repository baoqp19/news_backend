package com.example.news_backend.ui.bantin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.news_backend.data.models.BanTin
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.ui.save.SaveBanTinViewModel
import com.example.news_backend.utils.Constants
import com.example.news_backend.utils.Resource


@Composable
fun HotNewsSection(
    navController: NavController,
    viewModel: BanTinViewModel = viewModel(),
    saveViewModel: SaveBanTinViewModel = viewModel(),
    onOpenWebView: (String) -> Unit
) {
    val state by viewModel.listTinTuc.observeAsState()
    val colorScheme = MaterialTheme.colorScheme

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
                color = colorScheme.onBackground
            )
            Text(
                text = "Xem thêm",
                color =  Color(0xFFF1C30A),
                fontSize = 14.sp,
                modifier = Modifier.clickable {

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
                    CircularProgressIndicator(color = colorScheme.primary)
                }
            }

            is Resource.Error<*> -> {
                Text("Lỗi khi tải tin nóng", color = colorScheme.error)
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
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
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
                        tinTuc.link?.let { onClick(it) }
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
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = colorScheme.onSurface
                    ),
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
                        tint = Color(0xFFF1C30A),
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = tinTuc.pubDate,
                        fontSize = 12.sp,
                        color = colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "VnExpress",
                        fontSize = 12.sp,
                        color = colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Bottom)
                    )
                }
            }
        }
    }
}
