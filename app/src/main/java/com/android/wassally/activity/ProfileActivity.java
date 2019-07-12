package com.android.wassally.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wassally.Constants;
import com.android.wassally.R;
import com.android.wassally.model.User;
import com.android.wassally.networkUtils.UserClient;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private TextView mNameTextView, mUserNameTextView, mEmailTextView, mPhoneTextView, mAddressTextView, mEditTextView;
    private ProgressBar progressBar;
    private CircleImageView circularImageView;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        circularImageView = findViewById(R.id.profile_picture);
        mNameTextView = findViewById(R.id.profile_name);
        mUserNameTextView = findViewById(R.id.profile_userName);
        mEmailTextView = findViewById(R.id.tv_profile_email);
        mPhoneTextView = findViewById(R.id.tv_profile_phone);
        mAddressTextView = findViewById(R.id.tv_profile_address);
        mEditTextView = findViewById(R.id.edit_profile_tv);
        progressBar = findViewById(R.id.profile_progress);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mNameTextView.setText(preferences.getString(Constants.FULL_NAME, ""));

        getUserInfo();

        mEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra(Constants.USER_EXTRA, user);
                startActivity(intent);
            }
        });
    }

    private void getUserInfo() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = "Token " + preferences.getString(Constants.AUTH_TOKEN, "");

        // create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //get client and call object for the request
        UserClient client = retrofit.create(UserClient.class);
        Call<User> userCall = client.getUserInfo(token);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                mEditTextView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                //everything is ok just display these data
                if (response.isSuccessful() && response.body() != null) {
                    populateUserInfo(response);
                } else {
                    Toast.makeText(ProfileActivity.this, "error getting info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfileActivity.this, "something went wrong , check network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUserInfo(@NonNull Response<User> response) {
        assert response.body() != null;
        user = response.body();
        mNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        mUserNameTextView.setText(user.getUsername());
        mEmailTextView.setText(user.getEmail());
        mPhoneTextView.setText(user.getPhoneNumber());
        if (user.getAddresses().size() > 0) {
            mAddressTextView.setText(user.getAddresses().get(0).getFormatedAddress());
        }

        Picasso.get()
                .load(user.getImage())
                .error(R.drawable.man)
                .placeholder(R.drawable.profile)
                .into(circularImageView);
    }

}
