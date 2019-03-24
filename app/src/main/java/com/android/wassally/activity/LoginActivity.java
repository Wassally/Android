package com.android.wassally.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.wassally.R;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mLoginButton;
    private TextView msignUpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.email_sign_in_button);
        msignUpView = findViewById(R.id.tv_sign_up);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email =mEmailView.getText().toString().trim();
                String password = mPasswordView.getText().toString().trim();

                if(TextUtils.equals(email,"esam@gmail.com")&& TextUtils.equals(password,"esam123")){
                    Intent loadHome = new Intent(LoginActivity.this,ClientHomeActivity.class);
                    startActivity(loadHome);
                    finish();

                }
            }
        });

        msignUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
