package com.example.iikagennnishiro.ui

import android.icu.util.Calendar
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.iikagennnishiro.ui.theme.IikagennnishiroTheme

@OptIn(ExperimentalMaterial3Api::class) // ✅ 追加
@Composable
fun SalesScreen() {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    IikagennnishiroTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("売上入力") }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(text = "日付: $year 年 $month 月 $day 日")
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("売上金額") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { /* 保存処理 */ }) {
                    Text("売上保存")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSalesScreen() {
    IikagennnishiroTheme {
        SalesScreen()
    }
}
