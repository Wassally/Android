package com.android.wassally.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wassally.Constants;
import com.android.wassally.R;
import com.android.wassally.model.Addresses.Address;
import com.android.wassally.model.Addresses.Location;
import com.android.wassally.model.Addresses.PackageAddress;
import com.android.wassally.model.Order;
import com.android.wassally.networkUtils.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PackageSummaryActivity extends AppCompatActivity {
    private TextView mToLocationTextView;
    private TextView mToNumberTextView;
    private TextView mToNameTextView;
    private TextView mFromLocationTextView;
    private TextView mFromNumberTextView;
    private TextView mWeightTextView;
    private TextView mDurationTextView;
    private TextView mNoteTextView;
    private TextView mSubmitOrderButton;
    private TextView mSalaryTextView;

    private Dialog dialog;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_summary);

        mFromLocationTextView =findViewById(R.id.tv_from_location);
        mFromNumberTextView = findViewById(R.id.tv_from_number);
        mToLocationTextView = findViewById(R.id.tv_to_location);
        mToNameTextView = findViewById(R.id.tv_to_name);
        mToNumberTextView = findViewById(R.id.tv_to_number);
        mWeightTextView =findViewById(R.id.tv_weight);
        mDurationTextView = findViewById(R.id.tv_duration);
        mNoteTextView = findViewById(R.id.tv_note);
        mSubmitOrderButton = findViewById(R.id.submit_order_btn);
        mSalaryTextView = findViewById(R.id.tv_salary);

        Intent intent = getIntent();
        if(intent.hasExtra(Constants.SALARY_EXTRA)){
            mSalaryTextView.setText(String.valueOf(intent.getDoubleExtra(Constants.SALARY_EXTRA,0)));
            Log.i("mytag","salary is " + String.valueOf(intent.getIntExtra(Constants.SALARY_EXTRA,0)));
        }
        if (intent.hasExtra(Constants.ORDER_EXTRA)){
            order = intent.getParcelableExtra(Constants.ORDER_EXTRA);


            mToNameTextView.setText(order.getReceiverName());
            mToLocationTextView.setText(order.getPackageAddress().getToAddress().getFormatedAddress());
            mFromLocationTextView.setText(order.getPackageAddress().getFromAddress().getFormatedAddress());
            mToNumberTextView.setText(order.getReceiverPhoneNumber());
            mFromNumberTextView.setText(order.getSenderPhoneNumber());
            mToNameTextView.setText(order.getReceiverName());
            mWeightTextView.setText(String.valueOf(order.getWeight()));
            mDurationTextView.setText(String.valueOf(order.getDuration()));
            mNoteTextView.setText(order.getNote());
        }else {
            Log.i("mytag","error getting the order");
        }

        mSubmitOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order != null) {
                    showProgressDialog();
                    sendCreatePackageRequest(order);
                }else {
                    Toast.makeText(PackageSummaryActivity.this,"error with creating order",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendCreatePackageRequest(Order order){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PackageSummaryActivity.this);
        String token = "Token "+preferences.getString(Constants.AUTH_TOKEN,"");

        //create retrofit instance
         Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<Order> orderCall = client.createNewPackage(order,token);

        orderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    Toast.makeText(PackageSummaryActivity.this,"package created",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PackageSummaryActivity.this,ClientHomeActivity.class));
                    finish();
                }else {
                    Toast.makeText(PackageSummaryActivity.this,"error with creating new order",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(PackageSummaryActivity.this,"check network connection",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showProgressDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(R.layout.progress);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
