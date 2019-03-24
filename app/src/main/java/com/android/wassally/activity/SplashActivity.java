package com.android.wassally.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.wassally.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_logo_anim);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(500);
        animation.setStartOffset(500);
        final ImageView splashLogo = findViewById(R.id.splash_logo);
        splashLogo.startAnimation(animation);*/



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent postSplashIntent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(postSplashIntent);
                finish();
                // transition from splash to main menu
                overridePendingTransition(R.anim.splash_fade_out,
                        R.anim.login_fade_in);
            }
        },SPLASH_DISPLAY_LENGTH);

    }
}
