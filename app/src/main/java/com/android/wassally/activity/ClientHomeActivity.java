package com.android.wassally.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.wassally.R;

public class ClientHomeActivity extends AppCompatActivity {
    private TextView mClientNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        mClientNameTextView = findViewById(R.id.tv_clientName);

        String clientName = getIntent().getStringExtra("full_name");
        mClientNameTextView.setText(clientName);
    }
}
