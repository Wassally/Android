package com.android.wassally.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.wassally.Constants;
import com.android.wassally.R;
import com.android.wassally.model.SignUP;
import com.android.wassally.model.User;
import com.android.wassally.networkUtils.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private EditText mEmailEt;
    private EditText mUserNameEt;
    private EditText mPasswordEt;
    private EditText mPhoneNumberEt;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirstNameEt = findViewById(R.id.sign_up_first_name_et);
        mLastNameEt = findViewById(R.id.sign_up_last_name_et);
        mEmailEt = findViewById(R.id.sign_up_email_et);
        mPasswordEt = findViewById(R.id.sign_up_password_et);
        mPhoneNumberEt = findViewById(R.id.sign_up_phone_number_et);
        mUserNameEt = findViewById(R.id.sign_up_username_et);
        Button mSignUpButton = findViewById(R.id.sign_up_button);
        progressDialog = new ProgressDialog(this);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                         attemptSignUp();
            }
        });
    }

    private void sendSignUpNetworkRequest(SignUP signUP){
        //create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        // get client and call object for the request
        UserClient client = retrofit.create(UserClient.class);
        Call<User> call = client.createAccount(signUP);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                progressDialog.cancel();

                if(response.isSuccessful()&&response.body()!=null) {

                    String token = response.body().getToken();
                    String firstName = response.body().getFirstName();
                    String lastName = response.body().getLastName();
                    String fullName = firstName+" "+lastName;
                    Log.i("mytag","full name "+ fullName);

                    //save this token to sharedPreferences in order not to login every time user lunch the app
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                    preferences.edit().putString(Constants.AUTH_TOKEN, token).apply();
                    preferences.edit().putString(Constants.FULL_NAME,fullName).apply();

                    Toast.makeText(SignUpActivity.this, "Successful sign Up", Toast.LENGTH_SHORT).show();

                    //open home activity
                    Intent homeIntent = new Intent(SignUpActivity.this, ClientHomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.cancel();
                Toast.makeText(SignUpActivity.this,"some thing went wrong! :( ",Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Attempts to sign up new account specified by the sign up form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual sign up attempt is made.
     */

    private void attemptSignUp(){

        // Reset errors.
        mFirstNameEt.setError(null);
        mLastNameEt.setError(null);
        mEmailEt.setError(null);
        mPasswordEt.setError(null);
        mUserNameEt.setError(null);
        mPhoneNumberEt.setError(null);


        // Store values at the time of the sign up attempt.
        String firstName = mFirstNameEt.getText().toString();
        String lastName = mLastNameEt.getText().toString();
        String email = mEmailEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        String phoneNumber = mPhoneNumberEt.getText().toString();
        String username = mUserNameEt.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //check for phoneNumber
        if(TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberEt.setError(getString(R.string.error_field_required));
            focusView = mPhoneNumberEt;
            cancel = true;
        }

        // check for username
        if(TextUtils.isEmpty(username)){
            mUserNameEt.setError(getString(R.string.error_missing_username));
            focusView = mUserNameEt;
            cancel = true;
        }

        //check for password
        if(TextUtils.isEmpty(password)){
            mPasswordEt.setError(getString(R.string.error_missing_password));
            focusView = mPasswordEt;
            cancel = true;
        } else if (!isPasswordValid(password)){
            mPasswordEt.setError(getText(R.string.error_invalid_password));
            focusView = mPasswordEt;
            cancel = true;
        }

        //check for email
        if(TextUtils.isEmpty(email) || !isEmailValid(email)){
            mEmailEt.setError(getString(R.string.error_missing_email));
            focusView = mEmailEt;
            cancel = true;
        }

        //check for last name
        if (TextUtils.isEmpty(lastName)) {
            mLastNameEt.setError(getString(R.string.error_missing_last_name));
            focusView = mLastNameEt;
            cancel = true;
        }

        //check for first name
        if(TextUtils.isEmpty(firstName)) {
            mFirstNameEt.setError(getString(R.string.error_missing_first_name));
            focusView = mFirstNameEt;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt sign up and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else {
            progressDialog.setMessage("Signing Up ..");
            progressDialog.show();

            SignUP signUP = new SignUP(firstName, lastName, email, username, password,
                    phoneNumber,true,false);
            sendSignUpNetworkRequest(signUP);
        }

    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
