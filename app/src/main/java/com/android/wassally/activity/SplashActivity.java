package com.android.wassally.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.wassally.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private static final String AUTH_TOKEN = "auth_token";

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
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                String token = preferences.getString(AUTH_TOKEN,"");

                if (TextUtils.isEmpty(token)) {
                    Intent postSplashIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(postSplashIntent);

                    // transition from splash to main menu
                    overridePendingTransition(R.anim.splash_fade_out,
                            R.anim.login_fade_in);
                }else {
                    Intent postSplashIntent = new Intent(SplashActivity.this, ClientHomeActivity.class);
                    startActivity(postSplashIntent);
                }

                finish();
            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
