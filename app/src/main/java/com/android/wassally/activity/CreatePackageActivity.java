package com.android.wassally.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wassally.Constants;
import com.android.wassally.R;

public class CreatePackageActivity extends AppCompatActivity {

    private LinearLayout mPickupLocationLayout;
    private LinearLayout mDestinationLocationLayout;
    private TextView mPickupLocationTextView;
    private TextView mDestinationLocationTextView;

    private EditText mReceiverNameEditText;
    private EditText mSenderPhoneNumber;
    private EditText mReceiverPhoneNumber;
    private EditText mDeliveryTimeEditText;
    private EditText mPackageWeightEditText;
    private Spinner mTransTypeSpinner;
    private Button mCreatePackageButton;

    String [] transTypes = {"Car","Bicycle","Any"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mPickupLocationLayout = findViewById(R.id.pickup_location);
        mDestinationLocationLayout = findViewById(R.id.destination_location);
        mPickupLocationTextView = findViewById(R.id.tv_selected_pickup_location);
        mDestinationLocationTextView = findViewById(R.id.tv_selected_destination_location);
        mReceiverPhoneNumber = findViewById(R.id.et_receiver_phone_number);
        mReceiverNameEditText = findViewById(R.id.et_receiver_name);
        mSenderPhoneNumber = findViewById(R.id.et_sender_phone_number);
        mDeliveryTimeEditText = findViewById(R.id.et_delivery_time);
        mPackageWeightEditText = findViewById(R.id.et_weight);
        mTransTypeSpinner = findViewById(R.id.sp_trans_type);
        mCreatePackageButton = findViewById(R.id.btn_create_new_package);

        mPickupLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationPermissionOpenMapActivity(Constants.PICKUP_LOCATION_REQUEST);
            }
        });

        mDestinationLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationPermissionOpenMapActivity(Constants.DESTINATION_LOCATION_REQUEST);
            }
        });

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,transTypes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTransTypeSpinner.setAdapter(aa);

        mTransTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCreatePackageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewOrder();
            }
        });
    }

    private void checkLocationPermissionOpenMapActivity(int requestCode) {
        if (ContextCompat.checkSelfPermission(CreatePackageActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //location permission is not granted , ask for user permission
            startActivity(new Intent(CreatePackageActivity.this, PermissionActivity.class));

        } else {
            Intent intent = new Intent(CreatePackageActivity.this,NewOrderWithMapActivity.class);
            intent.putExtra(Constants.LOCATION_REQUEST_KEY,requestCode);
            startActivityForResult(intent,requestCode);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==Constants.RESULT_OK && requestCode==Constants.PICKUP_LOCATION_REQUEST){
            if(data.hasExtra("address")&&data.getExtras()!=null){
                mPickupLocationTextView.setText(data.getExtras().getString("address"));
            }
        }else if(resultCode==Constants.RESULT_OK &&requestCode==Constants.DESTINATION_LOCATION_REQUEST){
            if (data.hasExtra("address")&&data.getExtras()!=null){
                mDestinationLocationTextView.setText(data.getExtras().getString("address"));

            }
        }
    }

    private void createNewOrder(){

        boolean cancel = false;
        String message = "";


        String pickupAddress = mPickupLocationTextView.getText().toString();
        String destinationAddress = mDestinationLocationTextView.getText().toString();
        String receiverName = mReceiverNameEditText.getText().toString();
        String receiverPhoneNumber = mReceiverPhoneNumber.getText().toString();
        String senderPhoneNumber = mSenderPhoneNumber.getText().toString();
        String deliveryTime = mDeliveryTimeEditText.getText().toString();
        String packageWeight = mPackageWeightEditText.getText().toString();
        String transportType = mTransTypeSpinner.getSelectedItem().toString();


        if (TextUtils.isEmpty(packageWeight)){
            message =(getString(R.string.enter_approximate_weight));
            cancel =true;
        }

        if(TextUtils.isEmpty(deliveryTime)){
            message = (getString(R.string.enter_maximum_days));
            cancel =true;
        }

        if(TextUtils.isEmpty(senderPhoneNumber)){
            message = (getString(R.string.enter_sender_phone_number));
            cancel =true;
        }

        if(TextUtils.isEmpty(receiverPhoneNumber)){
            message =(getString(R.string.enter_receiver_phone_number));
            cancel =true;
        }

        if(TextUtils.isEmpty(receiverName)){
            message = (getString(R.string.enter_receiver_name));
            cancel =true;
        }

        if (destinationAddress.equals(getString(R.string.select_destination_location))){
            message = getString(R.string.select_destination_location);
            cancel =true;
        }

        if(pickupAddress.equals(getString(R.string.choose_pickup_location))){
            message = (getString(R.string.choose_pickup_location));
            cancel =true;
        }

        if(cancel){
            alertView(message);
        }else {
            Toast.makeText(CreatePackageActivity.this,"ready to create new order",Toast.LENGTH_SHORT).show();
        }

    }

    private void alertView(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Missing fields")
                .setMessage(message)
                .setIcon(R.drawable.ic_error)
                .setNegativeButton("Ok",null)
                .show();
    }

}
