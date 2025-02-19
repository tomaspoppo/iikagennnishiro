package com.example.iikagennnishiro.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.iikagennnishiro.utils.getFilteredSalesData
import com.example.iikagennnishiro.utils.getCurrentSalesStartDate
import com.example.iikagennnishiro.utils.getCustomDateRange
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesHistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("SalesData", Context.MODE_PRIVATE)

    var salesData by remember { mutableStateOf(emptyMap<String, Pair<String, String>>()) }
    var salesStartDate by remember { mutableStateOf("") }
    var salesEndDate by remember { mutableStateOf("") }
    var customDateRange by remember { mutableStateOf<Pair<String, String>?>(null) }
    var isCustomEnabled by remember { mutableStateOf(false) }
    var currentPeriod by remember { mutableStateOf("売上期間: 未設定") } // 🔹 現在の売上期間を表示する変数

    // 🔹 売上データを取得する関数（画面起動時＆ボタン押下時に使用）
    fun fetchSalesData() {
        val today = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN).format(Date())
        salesStartDate = getCurrentSalesStartDate(sharedPreferences, today)
        salesEndDate = sharedPreferences.getString("DefaultEndDate", "未設定") ?: "未設定"
        customDateRange = getCustomDateRange(sharedPreferences)
        isCustomEnabled = sharedPreferences.getBoolean("CustomEnabled", false)

        // 🔹 売上期間を UI に即時反映
        currentPeriod = if (isCustomEnabled && customDateRange != null) {
            "売上期間: ${customDateRange!!.first} ～ ${customDateRange!!.second}"
        } else {
            "売上期間: $salesStartDate ～ $salesEndDate"
        }

        // 🔹 売上データを取得
        salesData = if (isCustomEnabled && customDateRange != null) {
            getFilteredSalesData(sharedPreferences, customDateRange!!)
        } else {
            getFilteredSalesData(sharedPreferences, Pair(salesStartDate, salesEndDate))
        }
    }

    // 🔹 画面が開いたときに売上期間を表示＆データ取得
    LaunchedEffect(Unit) {
        fetchSalesData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("売上履歴", fontSize = 20.sp, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 現在の売上期間を表示（データ抽出ボタンの上）
                Text(
                    text = currentPeriod,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 データ抽出ボタン（手動更新用）
                Button(onClick = { fetchSalesData() }) {
                    Text("データ抽出", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 売上データの表示（LazyColumn）
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(salesData.toList()) { (date, data) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = date, fontSize = 18.sp, color = Color.Black)
                                Text(text = "売上: ¥${data.first}", fontSize = 16.sp, color = Color.Black)
                                Text(text = "労働時間: ${data.second}", fontSize = 16.sp, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    )
}
