package com.android.wassally.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wassally.R;
import com.android.wassally.helpers.DialogUtils;
import com.android.wassally.helpers.NetworkUtils;
import com.android.wassally.helpers.PreferenceUtils;
import com.android.wassally.model.Login;
import com.android.wassally.networkUtils.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText ;
    private EditText mPasswordEditText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = findViewById(R.id.login_email_et);
        mPasswordEditText = findViewById(R.id.login_password_et);
        TextView mSignUpTextView = findViewById(R.id.tv_sign_up);
        Button mLoginButton = findViewById(R.id.login_button);

        //when login button clicked start checking fields by calling attemptLogin method
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // if this is a new user he would click signUp -- so display signUp activity using explicit intent
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(){

        // Reset errors.
        mEmailEditText.setError(null);
        mPasswordEditText.setError(null);

        // Store values at the time of the login attempt.
        String email =mEmailEditText.getText().toString().trim();
        String password =mPasswordEditText.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        //check for a valid password if the user entered one
        if (TextUtils.isEmpty(password)){
            mPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = mPasswordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError(getString(R.string.error_field_required));
            focusView = mEmailEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else {
            // check for network state
            boolean isConnected = NetworkUtils.checkNetWorkConnectivity(this);

            // if connected start the network call
            if(isConnected) {
                DialogUtils.showDialog(this, getString(R.string.login_progress_message));

                Login login = new Login(email, password);
                sendLoginRequest(login);
            }else {
                Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * send login request to the server using retrofit
     * @param login object consist of (username or email) and password
     */
    private void sendLoginRequest(Login login){

        // create retrofit instance
        Retrofit retrofit = NetworkUtils.createRetrofitInstance();

        //get client and call object for the request
        UserClient client = retrofit.create(UserClient.class);
        Call<Login> loginCall = client.signIn(login);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call,@NonNull Response<Login> response) {
                DialogUtils.dismissDialog();
                if (response.isSuccessful() && response.body()!=null) {
                    //user is authorized
                    startHomeActivity(response);
                }else {
                    //password or username is incorrect
                    alertView();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                DialogUtils.dismissDialog();
                Toast.makeText(LoginActivity.this,"Login Failed, check network connection ",Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * after making successful network call to server
     * start the app by displaying home activity and finish this one
     * @param response the result of successful network call: Login object that contains basic user info
     */
    private void startHomeActivity(@NonNull Response<Login> response) {
        // extract basic user info name + auth token
        assert response.body() != null;
        String token = response.body().getToken();
        String firstName = response.body().getFirstName();
        String lastName = response.body().getLastName();
        String fullName = firstName+" "+lastName;

        //save this token to sharedPreferences in order not to login every time when user lunch the app
        PreferenceUtils.setFullName(fullName,this);
        PreferenceUtils.setToken(token,this);

        Toast.makeText(LoginActivity.this, getString(R.string.welcome_back)+" "+firstName, Toast.LENGTH_SHORT).show();

        // start home activity
        Intent loadHome = new Intent(LoginActivity.this, ClientHomeActivity.class);
        startActivity(loadHome);
        finish();
    }

    private void alertView() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getString(R.string.login_error_message))
                .setNegativeButton("Ok",null)
                .show();
    }
}
