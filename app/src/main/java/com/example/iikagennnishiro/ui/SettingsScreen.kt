package com.example.iikagennnishiro.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.iikagennnishiro.utils.saveCustomDateRange
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, onBackClick: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("SalesData", Context.MODE_PRIVATE)

    var customStartDate by remember { mutableStateOf("") }
    var showCustomDateDialog by remember { mutableStateOf(false) }
    var showFirstDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        println("⚙ 設定画面が開いた") // デバッグログ
        try {
            val today = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN).format(Date())
            customStartDate = sharedPreferences.getString("CustomStartDate", today) ?: "未設定"
        } catch (e: Exception) {
            println("エラー: ${e.localizedMessage}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定", fontSize = 20.sp, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("カスタム期間設定", fontSize = 18.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { showFirstDialog = true }) {
                    Text("カスタム期間を設定", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "現在のカスタム開始日: ${if (customStartDate.isNotEmpty()) customStartDate else "未設定"}",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                // 🔹 最初のダイアログ
                if (showFirstDialog) {
                    AlertDialog(
                        onDismissRequest = { showFirstDialog = false },
                        title = { Text("カレンダー表示") },
                        text = { Text("今からカレンダーが表示されます。売上開始日をタップしてください。") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showFirstDialog = false
                                    showCustomDateDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC0CB))
                            ) {
                                Text("はい")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showFirstDialog = false }) {
                                Text("いいえ")
                            }
                        }
                    )
                }

                // 🔹 カレンダー表示
                if (showCustomDateDialog) {
                    val calendar = Calendar.getInstance()

                    DatePickerDialog(
                        context,
                        { _, startYear, startMonth, startDayOfMonth ->
                            val startDate = "${startYear}年${startMonth + 1}月${startDayOfMonth}日"
                            customStartDate = startDate
                            saveCustomDateRange(context, startDate, startDate)
                            showCustomDateDialog = false
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
        }
    )
}
