package com.example.iikagennnishiro.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.example.iikagennnishiro.MainActivity;
import com.example.iikagennnishiro.R;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // ロゴのキラキラエフェクト（フェードイン・フェードアウト）
        ImageView logo = findViewById(R.id.splash_logo);

        AlphaAnimation fadeInOut = new AlphaAnimation(0.5f, 1.0f);
        fadeInOut.setDuration(1500); // 1.5秒で光る
        fadeInOut.setRepeatCount(Animation.INFINITE);
        fadeInOut.setRepeatMode(Animation.REVERSE);

        logo.startAnimation(fadeInOut);

        // 3.5秒後に MainActivity へ遷移
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3500);
    }
}
