package com.android.wassally.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wassally.Constants;
import com.android.wassally.R;
import com.android.wassally.helpers.NetworkUtils;
import com.android.wassally.helpers.PreferenceUtils;
import com.android.wassally.model.User;
import com.android.wassally.networkUtils.UserClient;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfileActivity extends AppCompatActivity {
    private EditText mFullNameEditText, mEmailEditText, mPhoneEditText, mAddressEditText;
    private TextView mSaveUpdatesTextView,mSelectAddressTextView,mUserNameTextView;
    private CircularImageView circularImageView;
    private ImageView updateProfile;
    private User user;
    private double latitude;
    private double longitude;
    private String addressDescription = "sfsgsdgs";
    private ProgressBar progressBar;

    Bitmap bitmap =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mUserNameTextView = findViewById(R.id.edit_profile_userName);
        mFullNameEditText = findViewById(R.id.edit_profile_name_et);
        mEmailEditText = findViewById(R.id.edit_profile_email_et);
        mPhoneEditText = findViewById(R.id.edit_profile_phone_et);
        mAddressEditText = findViewById(R.id.edit_profile_address_et);
        mSaveUpdatesTextView = findViewById(R.id.save_tv);
        circularImageView = findViewById(R.id.updated_profile_picture);
        updateProfile = findViewById(R.id.action_update_profile_iv);
        mSelectAddressTextView = findViewById(R.id.select_address_tv);
        progressBar = findViewById(R.id.edit_profile_progress);

        mAddressEditText.setFocusableInTouchMode(false);

        final Intent intent = getIntent();
        if(intent.hasExtra(Constants.USER_EXTRA)){
            user = intent.getParcelableExtra(Constants.USER_EXTRA);
            mFullNameEditText.setText(user.getFirstName()+" "+user.getLastName());
            mEmailEditText.setText(user.getEmail());
            mPhoneEditText.setText(user.getPhoneNumber());
            mUserNameTextView.setText(user.getUsername());
            if(user.getAddresses().size()>0) {
                mAddressEditText.setText(user.getAddresses().get(0).getFormatedAddress());
            }
            Picasso.get()
                    .load(user.getImage())
                    .error(R.drawable.profile)
                    .placeholder(R.drawable.profile)
                    .into(circularImageView);
        }else {
            Toast.makeText(this, "some thing went wrong!", Toast.LENGTH_SHORT).show();
        }

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI),Constants.GET_FROM_GALLERY);

            }
        });

        mSelectAddressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectLocationIntent = new Intent(EditProfileActivity.this,NewOrderWithMapActivity.class);
                selectLocationIntent.putExtra(Constants.GET_USER_ADDRESS,Constants.USER_ADDRESS_REQUEST);
                startActivityForResult(selectLocationIntent,Constants.USER_ADDRESS_REQUEST);
            }
        });

        mSaveUpdatesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptUpdateProfile();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Constants.GET_FROM_GALLERY && resultCode==Activity.RESULT_OK){
            Uri selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            circularImageView.setImageBitmap(bitmap);
        }
        if (requestCode==Constants.USER_ADDRESS_REQUEST&&resultCode==Constants.RESULT_OK){
            assert data != null;
            if (data.hasExtra(Constants.SELECTED_ADDRESS) && data.getExtras() != null) {
                mAddressEditText.setText(data.getExtras().getString(Constants.SELECTED_ADDRESS));
            }
            if (data.hasExtra(Constants.LOCATION_LAT_EXTRA)) {
                latitude = data.getExtras().getDouble(Constants.LOCATION_LAT_EXTRA);
            }
            if (data.hasExtra(Constants.LOCATION_LNG_EXTRA)) {
                longitude = data.getExtras().getDouble(Constants.LOCATION_LNG_EXTRA);
            }
        }
    }

    private void updateProfile(User user){

        String token = PreferenceUtils.getToken(this);

        // create retrofit instance
        Retrofit retrofit = NetworkUtils.createRetrofitInstance();

        //get client and call object for the request
        UserClient client = retrofit.create(UserClient.class);
        Call<User> userCall = client.updateUserInfo(user,token);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()&&response.body()!=null){
                    Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(EditProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(EditProfileActivity.this, "network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptUpdateProfile() {
        String fullname = mFullNameEditText.getText().toString();
        String arr [] = fullname.split(" ",2);
        String firstName = arr[0];
        String lastName = arr[1];

        User user;

        if (bitmap==null){
            user = new User(mEmailEditText.getText().toString(),
                    mUserNameTextView.getText().toString(),
                    firstName,
                    lastName,
                    mPhoneEditText.getText().toString());
        }else {

            user = new User(mEmailEditText.getText().toString(),
                    mUserNameTextView.getText().toString(),
                    firstName,
                    lastName,
                    mPhoneEditText.getText().toString(),
                    imageToString()
            );
        }
        updateProfile(user);
        progressBar.setVisibility(View.VISIBLE);
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imagByte = byteArrayOutputStream.toByteArray();
        String imageString =  Base64.encodeToString(imagByte, Base64.DEFAULT);
        Log.i("mytag","image String "+imageString);
        return imageString;
    }
}
