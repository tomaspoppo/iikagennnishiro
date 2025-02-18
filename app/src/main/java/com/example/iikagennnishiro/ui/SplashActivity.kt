package com.example.iikagennnishiro.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.iikagennnishiro.MainActivity
import com.example.iikagennnishiro.R
import com.airbnb.lottie.LottieAnimationView

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // スプラッシュ画面のレイアウトを適用
        setContentView(R.layout.activity_splash)

        // Lottieアニメーションを開始
        val lightEffect = findViewById<LottieAnimationView>(R.id.light_effect)
        lightEffect.playAnimation()

        // 3.5秒後に MainActivity へ遷移
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // スプラッシュ画面を閉じる
        }, 3500)
    }
}
