package com.example.news_backend.ui.football

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.news_backend.data.models.Football
import com.example.news_backend.utils.Resource

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
            .clip(RoundedCornerShape(12.dp)) // Clip toàn bộ card
            .clickable { onClick(football.url) }
            .background(MaterialTheme.colorScheme.surface) // Optional: nền theo theme
            .padding(8.dp) // padding bên trong viền
    ) {
        AsyncImage(
            model = football.thumbnail,
            contentDescription = football.title,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)), // Ảnh cũng bo góc nhẹ
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            football.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
