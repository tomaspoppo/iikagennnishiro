package com.example.iikagennnishiro.ui

import android.content.Context
import android.content.SharedPreferences
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
import androidx.compose.ui.unit.dp
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
    var workDuration by remember { mutableStateOf("未計算") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogKey by remember { mutableStateOf("") }
    var workCompleted by remember { mutableStateOf(returnTime != null) }

    LaunchedEffect(returnTime) {
        if (departureTime != null && returnTime != null) {
            workDuration = calculateWorkDuration(departureTime!!, returnTime!!)
            saveWorkDuration(sharedPreferences, today, workDuration)

            // 帰庫時間登録後、データをリセットして次の出庫準備
            sharedPreferences.edit().remove("start_time_$today").apply()
            sharedPreferences.edit().remove("end_time_$today").apply()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(
                    onClick = {
                        val currentTime = getCurrentDateTime() // 修正: 年月日 + 時間分を取得
                        saveTime(sharedPreferences, "${dialogKey}_$today", currentTime)
                        if (dialogKey == "start_time") {
                            departureTime = currentTime
                        } else {
                            returnTime = currentTime
                            workCompleted = true
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC0CB))
                ) {
                    Text("登録")
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
                        dialogMessage = "出庫登録しますか？確定すると時間の変更はできなくなります。"
                        dialogKey = "start_time"
                        showDialog = true
                    },
                    enabled = departureTime == null && !workCompleted,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (departureTime != null) "出庫済み" else "出庫時間登録", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "出庫時間: ${departureTime ?: "未登録"}", color = Color.Black)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        dialogMessage = "帰庫時間を登録しますか？※登録すると出庫・帰庫入力時間がリセットされデータに記録されます。"
                        dialogKey = "end_time"
                        showDialog = true
                    },
                    enabled = returnTime == null && departureTime != null,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (returnTime != null) "帰庫済み" else "帰庫時間登録", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "帰庫時間: ${returnTime ?: "未登録"}", color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "本日の労働時間: $workDuration", color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

fun calculateWorkDuration(start: String, end: String): String {
    val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN)
    val startDate = format.parse(start)
    val endDate = format.parse(end)
    val duration = (endDate.time - startDate.time) / (1000 * 60)
    val hours = duration / 60
    val minutes = duration % 60
    return "${hours}時間${minutes}分"
}

fun saveWorkDuration(sharedPreferences: SharedPreferences, date: String, duration: String) {
    sharedPreferences.edit().putString("work_duration_$date", duration).apply()
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN)
    return sdf.format(Date())
}

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN)
    return sdf.format(Date())
}

fun loadTime(sharedPreferences: SharedPreferences, key: String): String? {
    return sharedPreferences.getString(key, null)
}

fun saveTime(sharedPreferences: SharedPreferences, key: String, value: String) {
    sharedPreferences.edit().putString(key, value).apply()
}
