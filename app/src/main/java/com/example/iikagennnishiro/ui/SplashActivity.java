package com.example.iikagennnishiro.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.iikagennnishiro.MainActivity;
import com.example.iikagennnishiro.R;

import java.util.Random;

public class SplashActivity extends Activity {
    private static final int FIREFLY_COUNT = 300; // 🔥 ホタルの数（300個）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 📌 画面サイズを取得（ホタルの出現位置を決めるため）
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        // 🔥 ロゴ
        ImageView logo = findViewById(R.id.splash_screen_logo);
        int logoCenterX = screenWidth / 2; // ロゴのX座標（中央）
        int logoCenterY = screenHeight / 2; // ロゴのY座標（中央）

        // 🔥 ホタルを画面に追加するためのレイアウト
        FrameLayout rootLayout = findViewById(android.R.id.content);

        Random random = new Random();

        // 🔥 300個のホタル（光の粒）を動的に生成
        for (int i = 0; i < FIREFLY_COUNT; i++) {
            ImageView firefly = new ImageView(this);
            firefly.setImageResource(R.drawable.firefly_glow);
            firefly.setLayoutParams(new FrameLayout.LayoutParams(10 + random.nextInt(20), 10 + random.nextInt(20))); // サイズ 10~30dp
            firefly.setVisibility(View.VISIBLE);

            // 🔥 ホタルの出発位置を「四隅 & 画面周囲ランダム」に設定
            float startX, startY;

            switch (random.nextInt(4)) { // 0〜3 のランダムな数値で場所を決める
                case 0: // 左側
                    startX = -50;
                    startY = random.nextInt(screenHeight);
                    break;
                case 1: // 右側
                    startX = screenWidth + 50;
                    startY = random.nextInt(screenHeight);
                    break;
                case 2: // 上側
                    startX = random.nextInt(screenWidth);
                    startY = -50;
                    break;
                default: // 下側
                    startX = random.nextInt(screenWidth);
                    startY = screenHeight + 50;
                    break;
            }

            float endX = logoCenterX + random.nextInt(100) - 50; // ロゴ中心に向かう（少しズレあり）
            float endY = logoCenterY + random.nextInt(100) - 50;

            // 🔥 ホタルを画面に追加
            rootLayout.addView(firefly);

            // 🔥 ホタルをロゴの中心へ移動させる（1.5〜3.5秒で移動）
            ObjectAnimator moveX = ObjectAnimator.ofFloat(firefly, "translationX", startX, endX);
            ObjectAnimator moveY = ObjectAnimator.ofFloat(firefly, "translationY", startY, endY);
            moveX.setDuration(1500 + random.nextInt(2000)); // 1.5 ~ 3.5秒
            moveY.setDuration(1500 + random.nextInt(2000));

            moveX.start();
            moveY.start();
        }

        // 🔥 3.0秒後にホタルをフェードアウト
        new Handler().postDelayed(() -> {
            for (int i = 0; i < rootLayout.getChildCount(); i++) {
                View firefly = rootLayout.getChildAt(i);
                if (firefly instanceof ImageView && firefly != logo) {
                    AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                    fadeOut.setDuration(500);
                    firefly.startAnimation(fadeOut);
                    firefly.setVisibility(View.GONE);
                }
            }
        }, 3000);

        // 🔥 4.0秒後に MainActivity へ遷移
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 4000);
    }
}
