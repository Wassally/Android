package com.android.wassally.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wassally.Constants;
import com.android.wassally.R;
import com.android.wassally.databinding.ActivityPackageSummaryBinding;
import com.android.wassally.helpers.DialogUtils;
import com.android.wassally.helpers.NetworkUtils;
import com.android.wassally.helpers.PreferenceUtils;
import com.android.wassally.model.Order;
import com.android.wassally.networkUtils.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PackageSummaryActivity extends AppCompatActivity {
    private ActivityPackageSummaryBinding binding;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_summary);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_package_summary);

        Intent intent = getIntent();
        if(intent.hasExtra(Constants.SALARY_EXTRA)){
            binding.tvSalary.setText(String.valueOf(intent.getDoubleExtra(Constants.SALARY_EXTRA,0)));
        }
        if (intent.hasExtra(Constants.ORDER_EXTRA)){
            order = intent.getParcelableExtra(Constants.ORDER_EXTRA);
            binding.setOrder(order);
        }
        if (intent.hasExtra("display_details_only")){
            binding.submitOrderBtn.setVisibility(View.GONE);
            binding.salaryCv.setVisibility(View.GONE);
            binding.packageStateCv.setVisibility(View.VISIBLE);

            String state = order.getState();
            int resImgState = R.drawable.state_waiting;
            int resStateCaption = R.string.state_waiting_caption;

            switch (state){
                case "pending":
                    resImgState = R.drawable.state_pending;
                    resStateCaption = R.string.state_pending_caption;
                    break;
                case "on_way":
                    resImgState = R.drawable.state_on_way;
                    resStateCaption = R.string.state_on_way_caption;
                    break;
                case "delivered":
                    resImgState = R.drawable.state_delivered;
                    resStateCaption = R.string.state_delivered_caption;
                    break;
            }
            populatePackageState(resImgState,resStateCaption);
        }

        binding.submitOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order != null) {
                    DialogUtils.showDialog(PackageSummaryActivity.this,getString(R.string.creating_order_loading_message));
                    createNewOrder(order);
                }else {
                    Toast.makeText(PackageSummaryActivity.this,"error with creating order",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNewOrder(Order order){

        String token = "Token "+PreferenceUtils.getToken(this);

        //create retrofit instance
        Retrofit retrofit = NetworkUtils.createRetrofitInstance();
        UserClient client = retrofit.create(UserClient.class);
        Call<Order> orderCall = client.createNewPackage(order,token);

        orderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                DialogUtils.dismissDialog();
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
                DialogUtils.dismissDialog();
                Toast.makeText(PackageSummaryActivity.this,"check network connection",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populatePackageState(int resImg , int resString){
        binding.stateImage.setImageResource(resImg);
        binding.stateCaption.setText(resString);
    }
}
