package com.android.wassally.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.wassally.R;
import com.android.wassally.helpers.PreferenceUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_logo_anim);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(500);
        animation.setStartOffset(500);
        final ImageView splashLogo = findViewById(R.id.splash_logo);
        splashLogo.startAnimation(animation);*/

        super.onCreate(savedInstanceState);
        int SPLASH_DISPLAY_LENGTH = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String token = PreferenceUtils.getToken(SplashActivity.this);

                if (TextUtils.isEmpty(token)) {
                    Intent postSplashIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(postSplashIntent);

                    // transition from splash to main menu
                    overridePendingTransition(R.anim.splash_fade_out,
                            R.anim.login_fade_in);
                } else {
                    Intent postSplashIntent = new Intent(SplashActivity.this, ClientHomeActivity.class);
                    startActivity(postSplashIntent);
                }

                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
