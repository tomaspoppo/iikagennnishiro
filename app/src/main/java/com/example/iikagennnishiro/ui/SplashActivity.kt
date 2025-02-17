package com.example.iikagennnishiro.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.iikagennnishiro.MainActivity

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // すでに起動している場合は何もしない
        if (!isTaskRoot) {
            finish()
            return
        }

        // 2秒後に MainActivity へ遷移
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // スプラッシュ画面を閉じる
        }, 2000)
    }
}

