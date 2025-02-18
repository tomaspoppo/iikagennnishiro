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

    var defaultStartDate by remember { mutableStateOf("") }
    var customStartDate by remember { mutableStateOf("") }
    var customEndDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var datePickerMode by remember { mutableStateOf("") }
    var isCustomEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val today = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN).format(Date())
        defaultStartDate = sharedPreferences.getString("DefaultStartDate", "未設定") ?: "未設定"
        customStartDate = sharedPreferences.getString("CustomStartDate", today) ?: "未設定"
        customEndDate = sharedPreferences.getString("CustomEndDate", today) ?: "未設定"
        isCustomEnabled = sharedPreferences.getBoolean("CustomEnabled", false)
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
                // 🔹 スライドスイッチ
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("カスタム集計を有効にする", fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isCustomEnabled,
                        onCheckedChange = {
                            isCustomEnabled = it
                            sharedPreferences.edit().putBoolean("CustomEnabled", it).apply()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 デフォルト売上開始日設定
                Button(
                    onClick = {
                        datePickerMode = "default"
                        showDatePicker = true
                    },
                    enabled = !isCustomEnabled
                ) {
                    Text("デフォルト売上開始日設定", fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "デフォルト売上開始日: ${if (defaultStartDate.isNotEmpty()) defaultStartDate else "未設定"}",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 カスタム売上開始日設定
                Button(
                    onClick = {
                        datePickerMode = "customStart"
                        showDatePicker = true
                    },
                    enabled = isCustomEnabled
                ) {
                    Text("ｶｽﾀﾑ売上開始日設定", fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "ｶｽﾀﾑ売上開始日: ${if (customStartDate.isNotEmpty()) customStartDate else "未設定"}",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 カスタム売上締め日設定
                Button(
                    onClick = {
                        datePickerMode = "customEnd"
                        showDatePicker = true
                    },
                    enabled = isCustomEnabled
                ) {
                    Text("ｶｽﾀﾑ売上締め日設定", fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "ｶｽﾀﾑ売上締め日: ${if (customEndDate.isNotEmpty()) customEndDate else "未設定"}",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )

    // 🔹 カレンダーダイアログ（共通）
    if (showDatePicker) {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = "${year}年${month + 1}月${dayOfMonth}日"
                when (datePickerMode) {
                    "default" -> {
                        defaultStartDate = date
                        sharedPreferences.edit().putString("DefaultStartDate", date).apply()
                    }
                    "customStart" -> {
                        customStartDate = date
                        sharedPreferences.edit().putString("CustomStartDate", date).apply()
                    }
                    "customEnd" -> {
                        customEndDate = date
                        sharedPreferences.edit().putString("CustomEndDate", date).apply()
                    }
                }
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
