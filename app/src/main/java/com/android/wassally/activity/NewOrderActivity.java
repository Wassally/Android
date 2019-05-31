package com.android.wassally.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.wassally.R;
import com.android.wassally.UserInputValidation;
import com.android.wassally.model.Order;
import com.android.wassally.networkUtils.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewOrderActivity extends AppCompatActivity {

    private static final String BASE_URL ="https://wassally.herokuapp.com/api/" ;
    private static final String AUTH_TOKEN = "auth_token";

    private EditText mFromGovernate;
    private EditText mFromCity;
    private EditText mSenderAddress;
    private EditText mSenderPhoneNumber;
    private EditText mToGovernate;
    private EditText mToCity;
    private EditText mReceiverAddress;
    private EditText mReceiverName;
    private EditText mReceiverPhoneNumber;
    private EditText mMaxDays;
    private EditText mTransType;
    private EditText mPackageWeight;
    private EditText mNote;

    private Button mSubmitOrder;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        mFromGovernate = findViewById(R.id.sender_governate_et);
        mFromCity = findViewById(R.id.sender_city_et);
        mSenderAddress = findViewById(R.id.sender_address_et);
        mSenderPhoneNumber = findViewById(R.id.sender_phone_number_et);
        mToGovernate = findViewById(R.id.receiver_governate_et);
        mToCity = findViewById(R.id.receiver_city_et);
        mReceiverAddress = findViewById(R.id.receiver_address_et);
        mReceiverName = findViewById(R.id.receiver_name_et);
        mReceiverPhoneNumber = findViewById(R.id.receiver_phone_number_et);
        mMaxDays = findViewById(R.id.duration_et);
        mTransType = findViewById(R.id.transportation_et);
        mPackageWeight = findViewById(R.id.package_weight_et);
        mNote = findViewById(R.id.client_note_et);
        mSubmitOrder = findViewById(R.id.submit_new_order_button);

        progressDialog = new ProgressDialog(this);

        mSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewOrder();
            }
        });
    }

    private void createNewOrder() {

        String fromGovernate = mFromGovernate.getText().toString().trim();
        String fromCity = mFromCity.getText().toString().trim();
        String fromAddress = mSenderAddress.getText().toString().trim();
        String toGovernate = mToGovernate.getText().toString().trim();
        String senderPhoneNumber = mSenderPhoneNumber.getText().toString().trim();
        String toCity = mToCity.getText().toString().trim();
        String toAddress = mReceiverAddress.getText().toString().trim();
        String receiverName = mReceiverName.getText().toString().trim();
        String receiverPhoneNumber = mReceiverPhoneNumber.getText().toString().trim();
        String transWay = mTransType.getText().toString().trim();
        String note = mNote.getText().toString();
        int duration = 0;
        int packageWeight = 0;
        String durationString = mMaxDays.getText().toString();
        if (!TextUtils.isEmpty(durationString)){
             duration = Integer.parseInt(durationString);
        }

        String packageWeightString = mPackageWeight.getText().toString().trim();
        if(!TextUtils.isEmpty(packageWeightString)) {
             packageWeight = Integer.parseInt(packageWeightString);
        }

        // create new order object
        Order newOrder = new Order(fromGovernate,fromCity,fromAddress,senderPhoneNumber,toGovernate,toCity,toAddress,
                receiverName,receiverPhoneNumber,note,duration,packageWeight,transWay);

        //check for all fields to be completed
        boolean isAllValid = UserInputValidation.isNewOrderAllSet(newOrder);
        if(isAllValid){
            progressDialog.setMessage("Creating new order ..");
            progressDialog.show();
            sendNewOrderRequest(newOrder);
        }else {
            Toast.makeText(this,"please complete all fields!",Toast.LENGTH_SHORT).show();

        }
    }

    private void sendNewOrderRequest(Order order){
        //get user token
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NewOrderActivity.this);
        String token ="Token "+preferences.getString(AUTH_TOKEN,"");

        // create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        //get client and call object for the request
        UserClient client = retrofit.create(UserClient.class);
        Call<Order> newOrderCall = client.createNewPackage(order,token);

        newOrderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(NewOrderActivity.this, "package Created Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(NewOrderActivity.this, "there was a server problem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                progressDialog.cancel();
                Toast.makeText(NewOrderActivity.this, "there was a problem", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
