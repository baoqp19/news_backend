//package com.example.news_backend.ui.football
//
//@Composable
//fun FootballScreen() {
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp)
//        ) {
//
//            SectionTitle(
//                title = stringResource(R.string.tin_b_ng),
//                detail = stringResource(R.string.xem_th_m)
//            )
//
//            LazyRow {
//                items(10) { index ->
//                    NewsFootballItem() // tùy chỉnh
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            SectionTitle(
//                title = stringResource(R.string.tin_n_ng),
//                detail = stringResource(R.string.xem_th_m)
//            )
//
//            LazyColumn {
//                items(10) { index ->
//                    NewsHotItem() // tùy chỉnh
//                }
//            }
//        }
//
//        SupportButtons()
//    }
//}
//@Composable
//fun SectionTitle(title: String, detail: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                text = detail,
//                fontSize = 15.sp,
//                color = Color.Blue,
//                fontStyle = FontStyle.Italic
//            )
//            Icon(
//                painter = painterResource(id = R.drawable.ic_right_black_24dp),
//                contentDescription = null,
//                modifier = Modifier.padding(start = 5.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun NewsFootballItem() {
//    Card(
//        modifier = Modifier
//            .padding(end = 8.dp)
//            .size(200.dp, 120.dp),
//        shape = RoundedCornerShape(10.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        // Tùy nội dung
//        Text("Tin bóng đá")
//    }
//}
//
//@Composable
//fun NewsHotItem() {
//    Card(
//        modifier = Modifier
//            .padding(vertical = 4.dp)
//            .fillMaxWidth(),
//        shape = RoundedCornerShape(10.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Text("Tin nóng")
//    }
//}
//@Composable
//fun SupportButtons() {
//    val visible = remember { mutableStateOf(false) }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//
//        if (visible.value) {
//            Card(
//                shape = RoundedCornerShape(30.dp),
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(end = 80.dp, bottom = 80.dp)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(15.dp)
//                ) {
//                    Text(
//                        text = stringResource(R.string.txt_support_zalo),
//                        color = Color.Blue,
//                        modifier = Modifier.padding(end = 10.dp)
//                    )
//                    Icon(
//                        painter = painterResource(id = R.drawable.zalo_sharelogo),
//                        contentDescription = null,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            }
//
//            Card(
//                shape = RoundedCornerShape(30.dp),
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(end = 20.dp, bottom = 140.dp)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(15.dp)
//                ) {
//                    Text(
//                        text = stringResource(R.string.txt_support_facebook),
//                        color = Color.Blue,
//                        modifier = Modifier.padding(end = 10.dp)
//                    )
//                    Icon(
//                        painter = painterResource(id = R.drawable.logo_messenger_new),
//                        contentDescription = null,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            }
//        }
//
//        FloatingActionButton(
//            onClick = { visible.value = !visible.value },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp)
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_support_chat),
//                contentDescription = null
//            )
//        }
//    }
//}