package com.example.news_backend.ui.account.screen

import android.support.annotation.DrawableRes
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.news_backend.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.ui.menu.update.UpdateUserViewModel
import com.example.news_backend.utils.Resource

@Composable
fun UpdateUserScreen(
    navController: NavController,
    viewModel: UpdateUserViewModel = viewModel()
) {
    val context = LocalContext.current
    val updateUserState by viewModel.updateUser.observeAsState()

    val name = remember { mutableStateOf(DataLocalManager.getInstance().getInfoUserName() ?: "Người dùng") }
    val email = remember { mutableStateOf(DataLocalManager.getInstance().getInfoEmail() ?: "example@example.com") }
    val password = remember { mutableStateOf("") }

    // Success state
    val isSuccess = updateUserState is Resource.Success<*>

    // Error state
    val isError = updateUserState is Resource.Error<*>

    // Handle success, error or loading state
    if (isSuccess) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            Log.d("UpdateUserScreen", "Update success")  // Thêm log
            navController.popBackStack() // Nếu thành công, quay lại màn hình trước
        }
    }

    if (isError) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            Log.d("UpdateUserScreen", "Update failed")  // Thêm log
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1877F2))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cập Nhật Thông Tin",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // CardView section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Thông tin cá nhân",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C8EBD)
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomOutlinedTextField(
                    label = "Tên đăng nhập",
                    value = name.value,
                    onValueChange = { name.value = it },
                    leadingIcon = R.drawable.ic_baseline_person
                )

                CustomOutlinedTextField(
                    label = "Email",
                    value = email.value,
                    onValueChange = { email.value = it },
                    leadingIcon = R.drawable.email
                )

                CustomOutlinedTextField(
                    label = "Mật khẩu",
                    value = password.value,
                    onValueChange = { password.value = it },
                    leadingIcon = R.drawable.password,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Quên mật khẩu ?",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.updateUserInfo(name.value, email.value, password.value)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2C8EBD),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cập Nhật Thông Tin",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    @DrawableRes leadingIcon: Int,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = null,
                tint = Color(0xFF2C8EBD)
            )
        },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(16.dp),

        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = Color.DarkGray,
            focusedBorderColor = Color(0xFF2C8EBD),
            unfocusedBorderColor = Color.LightGray,
            focusedLeadingIconColor =   Color(0xFF2C8EBD),
            unfocusedLeadingIconColor = Color.Gray,
            cursorColor =   Color(0xFF2C8EBD),
        ),

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}