package com.example.news_backend.ui.account.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.news_backend.R
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.ui.account.AccountViewModel
import com.example.news_backend.utils.Resource


@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AccountViewModel
) {
    val context = LocalContext.current
    val loginState by viewModel.loginResult.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                    "Đăng nhập",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(
                    0xFF4250A6),
                    modifier = Modifier.fillMaxWidth(), // để Text chiếm hết chiều rộng
                    textAlign = TextAlign.Center        // căn giữa nội dung
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Email", fontSize = 12.sp)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("Nhập email...", color = Color.Gray) },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_person),
                            contentDescription = null,
                            tint = Color(0xFF616161)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = Color(0xFF32578D),
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        focusedLeadingIconColor =  Color(0xFF32578D),
                        unfocusedLeadingIconColor = Color.Gray,
                        cursorColor =  Color(0xFF32578D),
                    )
                )


                Spacer(modifier = Modifier.height(8.dp))

                Text("Mật Khẩu", fontSize = 12.sp)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("Nhập mật khẩu...", color = Color.Gray) },
                    visualTransformation = PasswordVisualTransformation(),
                    label = { Text("password") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id =  R.drawable.ic_baseline_lock_open),
                            contentDescription = null,
                            tint = Color(0xFF616161)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedBorderColor = Color(0xFF32578D),
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        focusedLeadingIconColor =  Color(0xFF32578D),
                        unfocusedLeadingIconColor = Color.Gray,
                        cursorColor =  Color(0xFF32578D),
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Quên mật khẩu?", fontSize = 14.sp)

                Spacer(modifier = Modifier.height(20.dp))
                Text("Đăng nhập với", modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { /* TODO: Facebook login */ }) {
                        Image(painterResource(R.drawable.facebook), contentDescription = "Facebook", modifier = Modifier.size(30.dp))
                    }
                    Spacer(modifier = Modifier.width(45.dp))
                    IconButton(onClick = { /* TODO: Google login */ }) {
                        Image(painterResource(R.drawable.googlelogo), contentDescription = "Google", modifier = Modifier.size(40.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Không có tài khoản?")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Đăng kí ngay !",
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.popBackStack() // Quay lại màn login
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(R.drawable.avatar_login_cr7),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.login(email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475AB2))
        ) {
            Text("Đăng Nhập", fontSize = 18.sp, color = Color.White)
        }
    }

    loginState?.let { resource ->
        when (resource) {
            is Resource.Loading<*> -> LoadingScreen(true)
            is Resource.Success<*> -> {
                resource.data?.let {
                    DataLocalManager.getInstance().setSaveUserInfo(
                        it.data.id, it.data.name, it.data.username, it.data.email, it.data.roles.first()
                    )
                }
                Toast.makeText(context, resource.message ?: "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is Resource.Error<*> -> {
                Toast.makeText(context, resource.message ?: "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun LoadingScreen(show: Boolean) {
    if (show) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}