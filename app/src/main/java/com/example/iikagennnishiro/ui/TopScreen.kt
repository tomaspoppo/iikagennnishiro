package com.example.iikagennnishiro.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.iikagennnishiro.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("WorkTimes", Context.MODE_PRIVATE)!!

    val today = getCurrentDate()
    var departureTime by remember { mutableStateOf(loadTime(sharedPreferences, "start_time_$today")) }
    var returnTime by remember { mutableStateOf(loadTime(sharedPreferences, "end_time_$today")) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogKey by remember { mutableStateOf("") }
    var isInitialRegistration by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(dialogTitle) },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(
                    onClick = {
                        val currentTime = getCurrentTime()
                        saveTime(sharedPreferences, "${dialogKey}_$today", currentTime)
                        if (dialogKey == "start_time") {
                            departureTime = currentTime
                        } else {
                            returnTime = currentTime
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC0CB))
                ) {
                    Text(if (isInitialRegistration) "登録する" else "変更する")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("キャンセル")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("settings") },
                containerColor = Color.Gray,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "設定",
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_screen_logo),
                    contentDescription = "TOP画面ロゴ",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate("sales_input") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("売上入力", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        dialogTitle = "出庫時間登録"
                        dialogMessage = if (departureTime.isNullOrEmpty()) "出庫時間を登録しますか？" else "出庫時間を変更しますか？"
                        dialogKey = "start_time"
                        isInitialRegistration = departureTime.isNullOrEmpty()
                        showDialog = true
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("出庫時間登録", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = departureTime ?: "未登録", color = Color.Black)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        dialogTitle = "帰庫時間登録"
                        dialogMessage = if (returnTime.isNullOrEmpty()) "帰庫時間を登録しますか？" else "帰庫時間を変更しますか？"
                        dialogKey = "end_time"
                        isInitialRegistration = returnTime.isNullOrEmpty()
                        showDialog = true
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("帰庫時間登録", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = returnTime ?: "未登録", color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

fun saveTime(sharedPreferences: SharedPreferences, key: String, value: String) {
    sharedPreferences.edit().putString(key, value).apply()
}

fun loadTime(sharedPreferences: SharedPreferences, key: String): String? {
    return sharedPreferences.getString(key, null)
}

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.JAPAN)
    return sdf.format(Date())
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN)
    return sdf.format(Date())
}

@Preview(showBackground = true)
@Composable
fun PreviewTopScreen() {
    TopScreen(navController = rememberNavController())
}
