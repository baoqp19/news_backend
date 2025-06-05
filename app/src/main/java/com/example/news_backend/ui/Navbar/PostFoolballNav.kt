package com.example.news_backend.ui.Navbar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import com.example.news_backend.network.request.FoolBallRequest
import com.example.news_backend.ui.football.FootballPostViewModel
import com.example.news_backend.ui.football.FootballViewModel
import com.example.news_backend.utils.Resource


@Composable
fun PostFoolBallScreen(
    viewModel: FootballPostViewModel = viewModel(),
    onSubmitSuccess: () -> Unit = {},
    viewModelview: FootballViewModel = viewModel(),
) {
    val context = LocalContext.current
    val postResult by viewModel.postResult.observeAsState()
    val state by viewModelview.footballNews.observeAsState()


    var title by remember { mutableStateOf("") }
    var thumbnail by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    // Quan sát kết quả và hiển thị thông báo
    LaunchedEffect(postResult) {
        when (postResult) {
            is Resource.Success<*> -> {
                Toast.makeText(context, "Đăng tin thành công", Toast.LENGTH_SHORT).show()
                onSubmitSuccess()
                title = ""
                thumbnail = ""
                url = ""
                date = ""
                viewModelview.fetchDataCallAPI()
            }

            is Resource.Error<*> -> {
                Toast.makeText(context, "Đăng tin thất bại: ${(postResult)}", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 10.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Đăng Tin Bóng Đá",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4250A6),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                PostField("Tiêu đề", "Nhập tiêu đề...", title) { title = it }
                PostField("Thumbnail", "Nhập URL ảnh...", thumbnail) { thumbnail = it }
                PostField("URL", "Nhập đường dẫn bài viết...", url) { url = it }
                PostField("Ngày đăng", "VD: 2025-05-25", date) { date = it }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val postRequest = FoolBallRequest(
                            title = title,
                            thumbnail = thumbnail,
                            url = url,
                            date = date
                        )
                        viewModel.postFootballNews(postRequest)

                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4250A6))
                ) {
                    Text(
                        text = "Đăng Tin",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                }
            }
        }
    }
}
