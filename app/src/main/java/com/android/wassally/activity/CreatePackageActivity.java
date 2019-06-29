package com.android.wassally.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wassally.Constants;
import com.android.wassally.R;
import com.android.wassally.model.Addresses.Address;
import com.android.wassally.model.Addresses.Location;
import com.android.wassally.model.Addresses.PackageAddress;
import com.android.wassally.model.ComputeSalary;
import com.android.wassally.model.Order;
import com.android.wassally.networkUtils.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreatePackageActivity extends AppCompatActivity {


    private TextView mPickupLocationTextView;
    private TextView mDestinationLocationTextView;
    private EditText mReceiverNameEditText;
    private EditText mSenderPhoneNumber;
    private EditText mReceiverPhoneNumber;
    private EditText mDeliveryTimeEditText;
    private EditText mPackageWeightEditText;
    private EditText mNoteEditText;
    private Button mContinueBtn;

    private double salary;

    private String pickupAddress;
    private String destinationAddress;

    private double toLatitude;
    private double toLongitude;
    private double fromLatitude;
    private double fromLongitude;
    private String toAddressDescription = "sfsgsdgs";
    private String fromAddressDescription = "sfsgsdgs";
    private Order order;

    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        LinearLayout mPickupLocationLayout = findViewById(R.id.pickup_location);
        LinearLayout mDestinationLocationLayout = findViewById(R.id.destination_location);
        mPickupLocationTextView = findViewById(R.id.tv_selected_pickup_location);
        mDestinationLocationTextView = findViewById(R.id.tv_selected_destination_location);
        mReceiverPhoneNumber = findViewById(R.id.et_receiver_phone_number);
        mReceiverNameEditText = findViewById(R.id.et_receiver_name);
        mSenderPhoneNumber = findViewById(R.id.et_sender_phone_number);
        mDeliveryTimeEditText = findViewById(R.id.et_delivery_time);
        mPackageWeightEditText = findViewById(R.id.et_weight);
        mNoteEditText = findViewById(R.id.et_note);

        mContinueBtn = findViewById(R.id.btn_continue);

        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserInputCalcSalary();
            }
        });

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

    }

    private void checkLocationPermissionOpenMapActivity(int requestCode) {
        if (ContextCompat.checkSelfPermission(CreatePackageActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //location permission is not granted , ask for user permission
            startActivity(new Intent(CreatePackageActivity.this, PermissionActivity.class));

        } else {
            Intent intent = new Intent(CreatePackageActivity.this, NewOrderWithMapActivity.class);
            intent.putExtra(Constants.LOCATION_REQUEST_KEY, requestCode);
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //the result is pickup location (from)
        if (resultCode == Constants.RESULT_OK && requestCode == Constants.PICKUP_LOCATION_REQUEST) {
            assert data != null;
            if (data.hasExtra(Constants.SELECTED_ADDRESS) && data.getExtras() != null) {
                mPickupLocationTextView.setText(data.getExtras().getString(Constants.SELECTED_ADDRESS));
            }
            if (data.hasExtra(Constants.LOCATION_LAT_EXTRA)) {
                fromLatitude = data.getExtras().getDouble(Constants.LOCATION_LAT_EXTRA);
            }
            if (data.hasExtra(Constants.LOCATION_LNG_EXTRA)) {
                fromLongitude = data.getExtras().getDouble(Constants.LOCATION_LNG_EXTRA);
            }

            //the result is destination location (to)
        } else if (resultCode == Constants.RESULT_OK && requestCode == Constants.DESTINATION_LOCATION_REQUEST) {
            assert data != null;
            if (data.hasExtra(Constants.SELECTED_ADDRESS) && data.getExtras() != null) {
                mDestinationLocationTextView.setText(data.getExtras().getString(Constants.SELECTED_ADDRESS));
            }
            if (data.hasExtra(Constants.LOCATION_LAT_EXTRA)) {
                toLatitude = data.getExtras().getDouble(Constants.LOCATION_LAT_EXTRA);
            }
            if (data.hasExtra(Constants.LOCATION_LNG_EXTRA)) {
                toLongitude = data.getExtras().getDouble(Constants.LOCATION_LNG_EXTRA);
            }
        }
    }

    private void checkUserInputCalcSalary() {

        boolean cancel = false;
        String message = "";

        pickupAddress = mPickupLocationTextView.getText().toString();
        destinationAddress = mDestinationLocationTextView.getText().toString();
        String receiverName = mReceiverNameEditText.getText().toString();
        String receiverPhoneNumber = mReceiverPhoneNumber.getText().toString();
        String senderPhoneNumber = mSenderPhoneNumber.getText().toString();
        String deliveryTime = mDeliveryTimeEditText.getText().toString();
        String packageWeight = mPackageWeightEditText.getText().toString();
        String note = mNoteEditText.getText().toString();

        int weight = 0;
        int duration = 0;


        if (TextUtils.isEmpty(packageWeight)) {
            message = (getString(R.string.enter_approximate_weight));
            cancel = true;
        } else {
            weight = Integer.parseInt(packageWeight);
        }

        if (TextUtils.isEmpty(deliveryTime)) {
            message = (getString(R.string.enter_maximum_days));
            cancel = true;
        } else {
            duration = Integer.parseInt(deliveryTime);
        }

        if (TextUtils.isEmpty(senderPhoneNumber)) {
            message = (getString(R.string.enter_sender_phone_number));
            cancel = true;
        }

        if (TextUtils.isEmpty(receiverPhoneNumber)) {
            message = (getString(R.string.enter_receiver_phone_number));
            cancel = true;
        }

        if (TextUtils.isEmpty(receiverName)) {
            message = (getString(R.string.enter_receiver_name));
            cancel = true;
        }

        if (destinationAddress.equals(getString(R.string.select_destination_location))) {
            message = getString(R.string.select_destination_location);
            cancel = true;
        }

        if (pickupAddress.equals(getString(R.string.choose_pickup_location))) {
            message = (getString(R.string.choose_pickup_location));
            cancel = true;
        }

        if (cancel) {
            alertView(message);
        } else {
            showProgressDialog();
            Location fromLocation = new Location(fromLatitude, fromLongitude);
            Location toLocation = new Location(toLatitude, toLongitude);

            ComputeSalary computeSalary = new ComputeSalary(destinationAddress, pickupAddress, toLocation, fromLocation, weight);
            getExpectedSalary(computeSalary);

            PackageAddress packageAddress = getPackageAddress();

            order = new Order(senderPhoneNumber,
                    receiverName,
                    receiverPhoneNumber,
                    note,
                    duration,
                    weight,
                    Constants.TRANSPORT_WAY,
                    packageAddress);
        }
    }

    private void getExpectedSalary(ComputeSalary computeSalary) {
        //create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit2 = builder.build();

        UserClient client = retrofit2.create(UserClient.class);
        Call<ComputeSalary> salaryCall = client.getExpectedSalary(computeSalary);

        salaryCall.enqueue(new Callback<ComputeSalary>() {
            @Override
            public void onResponse(@NonNull Call<ComputeSalary> call, @NonNull Response<ComputeSalary> response) {
                dialog.dismiss();
                if (response.isSuccessful()&& response.body()!=null) {
                    Log.i("mytag", "response is successful, salary is " + response.body().getSalary());

                    salary = response.body().getSalary();
                    displayPackageSummary();
                }else {
                    alertView("Distance should be greater than 2 Km");
                }
            }

            @Override
            public void onFailure(Call<ComputeSalary> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CreatePackageActivity.this, "some thing went wrong, please try agian", Toast.LENGTH_SHORT).show();
                Log.i("mytag", "failed "+t);
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

    private void alertView(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message)
                .setIcon(R.drawable.ic_error)
                .setNegativeButton("Ok", null)
                .show();
    }

    private PackageAddress getPackageAddress() {
        //from
        Location fromLocation = new Location(fromLatitude, fromLongitude);
        Address fromAddress = new Address(fromLocation, pickupAddress, fromAddressDescription);

        Location toLocation = new Location(toLatitude, toLongitude);
        Address toAddress = new Address(toLocation, destinationAddress, toAddressDescription);

        return new PackageAddress(toAddress, fromAddress);
    }

    private void displayPackageSummary() {
        Intent summaryIntent = new Intent(CreatePackageActivity.this, PackageSummaryActivity.class);
        summaryIntent.putExtra(Constants.SALARY_EXTRA, salary);
        summaryIntent.putExtra(Constants.ORDER_EXTRA, order);
        startActivity(summaryIntent);
    }
}
