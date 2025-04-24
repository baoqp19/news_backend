package com.example.news_backend.ui.menu

import android.support.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.news_backend.R


@Composable
fun SettingsDialogContent(
    navController: NavController,
    onChangePassword: () -> Unit,
    onUpdateInfo: () -> Unit,
    onDeleteInfo: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = "Tuỳ chọn tài khoản",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            SettingsDialogButton(
                text = "🔒  Đổi Mật Khẩu",
                iconRes = R.drawable.passwordlast, // Sử dụng emoji cho tối giản
                onClick = onChangePassword
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color.Gray.copy(alpha = 0.3f)
            )

            SettingsDialogButton(
                text = "👤  Cập Nhật Thông Tin",
                iconRes = R.drawable.ic_person,
                onClick = {
                    navController.navigate("userinfo")
                }
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color.Gray.copy(alpha = 0.3f)
            )

            SettingsDialogButton(
                text = "🗑️  Xoá Tài Khoản",
                iconRes = R.drawable.logout,
                onClick = {
                    navController.navigate("userinfo")
                },
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


@Composable
fun SettingsDialogButton(
    text: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Sử dụng màu nền nổi bật với độ sáng nhẹ khi tương tác
    val customButtonColor = MaterialTheme.colorScheme.primary
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null, // Loại bỏ hiệu ứng mặc định của ripple
                interactionSource = interactionSource
            )
            .background(
                color = if (pressed) customButtonColor.copy(alpha = 0.2f) else customButtonColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp) // Điều chỉnh padding bên trong
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Hiển thị văn bản
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.weight(1f)
            )

            // Hiển thị biểu tượng (Icon)
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
    }
}