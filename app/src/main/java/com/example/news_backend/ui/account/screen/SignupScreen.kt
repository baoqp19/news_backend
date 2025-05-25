import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.utils.Resource
import com.example.news_backend.ui.account.AccountViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavHostController
import com.example.news_backend.R // thay thế bằng tên package của bạn


@Composable
fun SignupScreen(
    navController: NavHostController,
    viewModel: AccountViewModel
) {
    val context = LocalContext.current
    val signupState by viewModel.signupResult.observeAsState()

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val showProgress = signupState is Resource.Loading
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tiêu đề
                Text(
                    text = "Đăng ký tài khoản",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4250A6),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // Avatar
                Image(
                    painter = painterResource(id = R.drawable.avatar_pre),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(60.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Trường Họ tên
                CustomOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Họ tên",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tên đăng nhập
                CustomOutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Tên đăng nhập",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Email
                CustomOutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Mật khẩu
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Person else Icons.Default.Person,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF32578D),
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        focusedLeadingIconColor = Color(0xFF32578D),
                        unfocusedLeadingIconColor = Color.Gray,
                        focusedLabelColor = Color(0xFF32578D),
                        cursorColor = Color(0xFF32578D),
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Checkbox điều khoản
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = acceptedTerms,
                        onCheckedChange = { acceptedTerms = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF475AB2))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chấp nhận các điều khoản và điều kiện")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nút Đăng ký
                Button(
                    onClick = {
                        if (name.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT)
                                .show()
                        } else if (!acceptedTerms) {
                            Toast.makeText(
                                context,
                                "Bạn phải chấp nhận điều khoản",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.signup(name, username, email, password)
                            navController.navigate("login")
                            Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475AB2))
                ) {
                    Text("Đăng ký", fontSize = 18.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

// Hiển thị Progress khi đang xử lý đăng ký
        if (showProgress) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

// Xử lý kết quả đăng ký
        signupState?.let { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        DataLocalManager.getInstance().setSaveUserInfo(
                            it.data.id,
                            it.data.name,
                            it.data.username,
                            it.data.email,
                            it.data.roles.first()
                        )
                    }
                    Toast.makeText(
                        context,
                        result.message ?: "Đăng ký thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        result.message ?: "Đăng ký thất bại",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }
        }

    }       }



@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(icon, contentDescription = null)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor =  Color(0xFF32578D),
            unfocusedBorderColor = Color(0xFFBDBDBD),
            focusedLeadingIconColor =  Color(0xFF32578D),
            unfocusedLeadingIconColor = Color.Gray,
            focusedLabelColor =  Color(0xFF32578D),
            cursorColor =  Color(0xFF32578D),
        )
    )
}