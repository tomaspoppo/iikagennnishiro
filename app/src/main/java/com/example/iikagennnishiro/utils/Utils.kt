package com.example.iikagennnishiro.utils

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

/**
 * 売上開始日を取得
 */
fun loadSalesStartDate(sharedPreferences: SharedPreferences): String {
    return sharedPreferences.getString("SalesStartDate", "未設定") ?: "未設定"
}

/**
 * 売上開始日を保存
 */
fun saveSalesStartDate(context: Context, date: String) {
    val sharedPreferences = context.getSharedPreferences("SalesData", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("SalesStartDate", date).apply()
}

/**
 * カスタム売上期間を保存
 */
fun saveCustomDateRange(context: Context, startDate: String, endDate: String) {
    val sharedPreferences = context.getSharedPreferences("SalesData", Context.MODE_PRIVATE)
    sharedPreferences.edit()
        .putString("CustomStartDate", startDate)
        .putString("CustomEndDate", endDate)
        .apply()
}

/**
 * カスタム売上期間を取得
 */
fun loadCustomDateRange(sharedPreferences: SharedPreferences): Pair<String, String>? {
    val startDate = sharedPreferences.getString("CustomStartDate", null)
    val endDate = sharedPreferences.getString("CustomEndDate", null)
    return if (startDate != null && endDate != null) Pair(startDate, endDate) else null
}

/**
 * 売上開始日から次の売上開始日の前日までのデータを取得
 */
fun getFilteredSalesData(
    sharedPreferences: SharedPreferences,
    customRange: Pair<String, String>?
): Map<String, Pair<String, String>> {
    val allData = sharedPreferences.all.mapValues { it.value.toString() }
    val filteredData = mutableMapOf<String, Pair<String, String>>()
    val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN)

    val (startDateStr, endDateStr) = customRange
        ?: run {
            val today = dateFormat.format(Date())
            val start = getCurrentSalesStartDate(sharedPreferences, today)
            val end = getNextSalesStartDate(sharedPreferences, start)
            Pair(start, end)
        }

    val startDate = try {
        dateFormat.parse(startDateStr) ?: Date()
    } catch (e: Exception) {
        Date()
    }

    val endDate = try {
        dateFormat.parse(endDateStr) ?: Date()
    } catch (e: Exception) {
        Date()
    }

    val calendar = Calendar.getInstance()
    calendar.time = startDate

    while (!calendar.time.after(endDate)) {
        val date = dateFormat.format(calendar.time)
        allData[date]?.let { value ->
            val splitValues = value.split(",")
            if (splitValues.size == 2) {
                filteredData[date] = Pair(splitValues[0], splitValues[1]) // 売上金額, 労働時間
            } else {
                filteredData[date] = Pair(value, "0時間0分") // 労働時間がない場合デフォルトを設定
            }
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return filteredData.toSortedMap(compareByDescending { it })
}

/**
 * 特定の月の売上開始日を取得（設定されていない場合は通常の売上開始日を使用）
 */
fun getCurrentSalesStartDate(sharedPreferences: SharedPreferences, targetDate: String): String {
    val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN)
    val targetCalendar = Calendar.getInstance()
    targetCalendar.time = try {
        dateFormat.parse(targetDate) ?: Date()
    } catch (e: Exception) {
        Date()
    }

    val monthKey = SimpleDateFormat("yyyy-MM", Locale.JAPAN).format(targetCalendar.time) // 例: "2025-02"

    return loadSpecialSalesStartDate(sharedPreferences, monthKey)
        ?: loadSalesStartDate(sharedPreferences)
}

/**
 * 次の売上開始日を取得
 */
fun getNextSalesStartDate(sharedPreferences: SharedPreferences, startDate: String): String {
    val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN)
    val calendar = Calendar.getInstance()
    calendar.time = try {
        dateFormat.parse(startDate) ?: Date()
    } catch (e: Exception) {
        Date()
    }

    while (true) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val nextDateKey = SimpleDateFormat("yyyy-MM", Locale.JAPAN).format(calendar.time)
        val nextStartDate = loadSpecialSalesStartDate(sharedPreferences, nextDateKey)

        if (nextStartDate != null) {
            return nextStartDate
        }

        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return loadSalesStartDate(sharedPreferences)
        }
    }
}

/**
 * 設定されたカスタム期間を取得
 */
fun getCustomDateRange(sharedPreferences: SharedPreferences): Pair<String, String>? {
    return loadCustomDateRange(sharedPreferences)
}

/**
 * 特定の月の売上開始日を保存
 */
fun saveSpecialSalesStartDate(context: Context, monthKey: String, date: String) {
    val sharedPreferences = context.getSharedPreferences("SalesData", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("SpecialSalesStartDate_$monthKey", date).apply()
}

/**
 * 特定の月の売上開始日を取得
 */
fun loadSpecialSalesStartDate(sharedPreferences: SharedPreferences, monthKey: String): String? {
    return sharedPreferences.getString("SpecialSalesStartDate_$monthKey", null)
}

/**
 * 特定の月の売上開始日を削除
 */
fun clearSpecialSalesStartDate(context: Context, monthKey: String) {
    val sharedPreferences = context.getSharedPreferences("SalesData", Context.MODE_PRIVATE)
    sharedPreferences.edit().remove("SpecialSalesStartDate_$monthKey").apply()
}
