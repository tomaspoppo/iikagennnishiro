package com.example.iikagennnishiro.ui

import android.content.Context
import android.content.SharedPreferences
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

    var salesData by remember { mutableStateOf(emptyMap<String, String>()) }
    var salesStartDate by remember { mutableStateOf("") }
    var customDateRange by remember { mutableStateOf<Pair<String, String>?>(null) }

    LaunchedEffect(Unit) {
        val today = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN).format(Date())
        salesStartDate = getCurrentSalesStartDate(sharedPreferences, today)
        customDateRange = getCustomDateRange(sharedPreferences)

        salesData = if (customDateRange != null) {
            getFilteredSalesData(sharedPreferences, customDateRange!!)
        } else {
            getFilteredSalesData(sharedPreferences, Pair(salesStartDate, ""))
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
                Text("売上履歴", fontSize = 18.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text("現在の売上開始日: $salesStartDate", fontSize = 16.sp, color = Color.Black)
                if (customDateRange != null) {
                    Text("カスタム期間: ${customDateRange!!.first} 〜 ${customDateRange!!.second}", fontSize = 16.sp, color = Color.Red)
                }
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(salesData.toList()) { (date, amount) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = date, fontSize = 18.sp, color = Color.Black)
                                Text(text = "売上: ¥$amount", fontSize = 16.sp, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    )
}
