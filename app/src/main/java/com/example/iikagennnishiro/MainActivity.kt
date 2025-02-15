package com.example.iikagennnishiro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.iikagennnishiro.ui.TopScreen
import com.example.iikagennnishiro.ui.SalesInputScreen
import com.example.iikagennnishiro.ui.SettingsScreen
import com.example.iikagennnishiro.ui.SalesHistoryScreen
import com.example.iikagennnishiro.ui.theme.IikagennnishiroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IikagennnishiroTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "top") {
        composable(route = "top") {
            TopScreen(navController)
        }

        composable(route = "sales_input") {
            SalesInputScreen(
                navController = navController,
                onBackClick = { navController.navigate("top") }
            )
        }

        composable(route = "sales_history") {
            SalesHistoryScreen(navController)
        }

        composable(route = "settings") {
            SettingsScreen(
                navController = navController,
                onBackClick = { navController.navigate("top") } // ← ここを修正
            )
        }
    }
}
