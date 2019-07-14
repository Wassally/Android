package com.android.wassally.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.wassally.R;
import com.android.wassally.helpers.DialogUtils;
import com.android.wassally.helpers.ErrorUtils;
import com.android.wassally.helpers.NetworkUtils;
import com.android.wassally.helpers.PreferenceUtils;
import com.android.wassally.model.ApiError;
import com.android.wassally.model.SignUP;
import com.android.wassally.model.User;
import com.android.wassally.networkUtils.UserClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    public static Retrofit retrofit = NetworkUtils.createRetrofitInstance();

    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private EditText mEmailEt;
    private EditText mUserNameEt;
    private EditText mPasswordEt;
    private EditText mPhoneNumberEt;

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

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                         attemptSignUp();
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
            // everything is ready but ..
            // check network connectivity before sending any network calls
            boolean isConnected = NetworkUtils.checkNetWorkConnectivity(this);
            if(isConnected) {
                DialogUtils.showDialog(this, getString(R.string.sign_up_prgress_message));

                SignUP signUP = new SignUP(firstName, lastName, email, username, password,
                        phoneNumber, true, false);
                sendSignUpNetworkRequest(signUP);
            }else {
                Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * send signUp request to the server using retrofit and handling the response
     * @param signUP object contains user input data
     */
    private void sendSignUpNetworkRequest(SignUP signUP){
        // get client and call object for the request
        UserClient client = retrofit.create(UserClient.class);
        Call<User> call = client.createAccount(signUP);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                DialogUtils.dismissDialog();
                if(response.isSuccessful()&&response.body()!=null) {
                    //successfully created new user account
                    startHomeActivity(response);
                }else if (response.code()==400){
                    //there is some error with user input data ex:(this user name already exists)
                    extractErrorMessage(response);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                DialogUtils.dismissDialog();
                Toast.makeText(SignUpActivity.this,"some thing went wrong!, check your network connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * after making successful network call to server
     * start the app by displaying home activity and finish this one
     * @param response the result of successful network call: Login object that contains basic user info
     */
    private void startHomeActivity(@NonNull Response<User> response) {
        assert response.body() != null;
        // get token and user full name
        String token = response.body().getToken();
        String firstName = response.body().getFirstName();
        String lastName = response.body().getLastName();
        String fullName = firstName + " " + lastName;

        //save this token to sharedPreferences in order not to login every time user lunch the app
        PreferenceUtils.setToken(token,this);
        PreferenceUtils.setFullName(fullName,this);

        Toast.makeText(SignUpActivity.this, getString(R.string.welcome)+firstName, Toast.LENGTH_SHORT).show();

        //open home activity
        Intent homeIntent = new Intent(SignUpActivity.this, ClientHomeActivity.class);
        startActivity(homeIntent);
        finish();
    }

    private void extractErrorMessage(Response<?> response) {

        mEmailEt.setError(null);
        mPasswordEt.setError(null);
        mUserNameEt.setError(null);

        View focusView = null;

        ApiError apiError = ErrorUtils.parseError(response);
        ArrayList<String> errors =apiError.getErrors();
        String errorText;

        for (int i=errors.size()-1;i>=0;i--){
            errorText = errors.get(i);
            String arr [] = errorText.split(":",2);
            String head = arr[0];
            String message = arr[1];
            if (head.contains("email")){
                mEmailEt.setError(message);
                focusView= mEmailEt;

            }else if(head.contains("username")){
                mUserNameEt.setError(message);
                focusView = mUserNameEt;

            }else if(head.contains("password")) {
                mPasswordEt.setError(message);
                focusView = mPasswordEt;
            }
        }
        focusView.requestFocus();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@")&& email.contains(".com");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }
}
