package com.example.iikagennnishiro.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.iikagennnishiro.R

// ✅ 英語用フォント（Fredoka One）
val popFontFamily = FontFamily(
    Font(R.font.fredoka_one_regular, FontWeight.Normal)
)

// ✅ 日本語用フォント（Noto Sans JP）
val japaneseFontFamily = FontFamily(
    Font(R.font.noto_sans_jp_regular, FontWeight.Normal)
)

// ✅ 英語と日本語の混合フォント
val mixedFontFamily = FontFamily(
    Font(R.font.fredoka_one_regular, FontWeight.Normal), // ✅ 英語
    Font(R.font.noto_sans_jp_regular, FontWeight.Normal) // ✅ 日本語
)

// ✅ `Typography` のインスタンスを作成（final クラスのため、継承ではなく `val` で定義）
val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = mixedFontFamily,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    ),
    bodyLarge = TextStyle(
        fontFamily = mixedFontFamily,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = mixedFontFamily,
        fontSize = 14.sp
    )
)
