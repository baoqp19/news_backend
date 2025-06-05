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

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import com.example.news_backend.network.request.PostNewsLetterRequest
import com.example.news_backend.ui.home.viewmodel.PostNewsLetterViewModel
import com.example.news_backend.utils.Resource

@Composable
fun PostNewsScreen(
    viewModel: PostNewsLetterViewModel = viewModel(),
    onSubmitSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val postResult by viewModel.postNewsLetterResult.observeAsState()

    var title by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var img by remember { mutableStateOf("") }
    var pubDate by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    // Quan sát kết quả và hiển thị thông báo
    LaunchedEffect(postResult) {
        when (postResult) {
            is Resource.Success -> {
                Toast.makeText(context, "Đăng tin thành công", Toast.LENGTH_SHORT).show()
                onSubmitSuccess()
                // Clear input sau khi gửi thành công (tùy chọn)
                title = ""
                link = ""
                img = ""
                pubDate = ""
                category = ""
            }

            is Resource.Error -> {
                Toast.makeText(context, "Đăng tin thất bại: ${(postResult as Resource.Error).message}", Toast.LENGTH_SHORT).show()
            }

            else -> {} // Loading hoặc null thì không làm gì
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
                    "Đăng Tin Tức",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4250A6),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                PostField("Tiêu đề", "Nhập tiêu đề...", title) { title = it }
                PostField("Link", "Nhập link bài viết...", link) { link = it }
                PostField("Hình ảnh", "Nhập URL ảnh...", img) { img = it }
                PostField("Ngày đăng", "VD: 2025-05-17", pubDate) { pubDate = it }
                PostField("Danh mục", "VD: tin-the-gioi", category) { category = it }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val postRequest = PostNewsLetterRequest(
                            title = title,
                            link = link,
                            img = img,
                            pubDate = pubDate,
                            category = category
                        )
                        viewModel.postNewsLetter(postRequest)
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


@Composable
fun PostField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val customFont = FontFamily.SansSerif // Bạn có thể thay thành SansSerif, Monospace, Cursive...

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = customFont,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = customFont,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                )
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = customFont,
                fontSize = 16.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}