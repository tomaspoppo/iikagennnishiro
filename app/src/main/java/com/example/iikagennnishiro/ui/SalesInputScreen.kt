package com.example.iikagennnishiro.ui

import android.content.Context
import android.content.SharedPreferences
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.focus.onFocusChanged
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesInputScreen(navController: NavController, onBackClick: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("SalesData", Context.MODE_PRIVATE)
    val calendar = Calendar.getInstance()

    var selectedDate by remember { mutableStateOf(getFormattedDate(calendar)) }
    var selectedAmount by remember { mutableStateOf(loadSalesAmount(sharedPreferences, selectedDate)) }
    var showDialog by remember { mutableStateOf(false) }
    var isAmountFieldFocused by remember { mutableStateOf(false) }
    var confirmDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("売上入力", color = Color.Black, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedAmount,
                    onValueChange = { selectedAmount = it },
                    label = { Text(if (isAmountFieldFocused) selectedDate else "売上金額", color = Color.Black) },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState -> isAmountFieldFocused = focusState.isFocused },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (selectedAmount.isNotEmpty()) {
                            confirmDialogVisible = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC0CB))
                ) {
                    Text("売上保存", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("sales_history") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("売上履歴", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.weight(1f))

                AndroidView(
                    factory = { ctx -> CalendarView(ctx).apply {
                        setOnDateChangeListener { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            selectedDate = getFormattedDate(calendar)
                            selectedAmount = loadSalesAmount(sharedPreferences, selectedDate)
                        }
                    } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    )

    if (confirmDialogVisible) {
        AlertDialog(
            onDismissRequest = { confirmDialogVisible = false },
            title = { Text("確認") },
            text = { Text("￥${selectedAmount}で保存しますか？") },
            confirmButton = {
                Button(
                    onClick = {
                        saveSalesAmount(context, selectedDate, selectedAmount) {
                            selectedAmount = ""
                            confirmDialogVisible = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC0CB))
                ) {
                    Text("はい")
                }
            },
            dismissButton = {
                Button(
                    onClick = { confirmDialogVisible = false }
                ) {
                    Text("いいえ")
                }
            }
        )
    }
}

fun loadSalesAmount(sharedPreferences: SharedPreferences, date: String): String {
    return sharedPreferences.getString(date, "") ?: ""
}

fun saveSalesAmount(context: Context, date: String, amount: String, onComplete: () -> Unit) {
    val sharedPreferences = context.getSharedPreferences("SalesData", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString(date, amount).apply()
    Log.d("SalesInputScreen", "保存: $date -> ¥$amount")
    onComplete()
}

fun getFormattedDate(calendar: Calendar): String {
    val format = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN)
    return format.format(calendar.time)
}
