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

    // 🔹 データ取得関数
    fun fetchSalesData() {
        val today = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN).format(Date())
        salesStartDate = sharedPreferences.getString("DefaultStartDate", today) ?: today
        salesEndDate = sharedPreferences.getString("DefaultEndDate", getNextClosingDate(salesStartDate)) ?: getNextClosingDate(salesStartDate)
        customDateRange = getCustomDateRange(sharedPreferences)
        isCustomEnabled = sharedPreferences.getBoolean("CustomEnabled", false)

        salesData = if (isCustomEnabled && customDateRange != null) {
            getFilteredSalesData(sharedPreferences, customDateRange!!)
        } else {
            getFilteredSalesData(sharedPreferences, Pair(salesStartDate, salesEndDate))
        }
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

                // 🔹 データ抽出ボタン
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

// 🔹 次の売上締め日を取得（開始日の前日）
fun getNextClosingDate(startDate: String): String {
    if (startDate.isEmpty()) return "不明"
    val sdf = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN)
    return try {
        val calendar = Calendar.getInstance()
        calendar.time = sdf.parse(startDate)!!
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        sdf.format(calendar.time)
    } catch (e: Exception) {
        "不明"
    }
}
