package com.android.wassally.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.android.wassally.databinding.ActivityLoginBinding;
import com.android.wassally.model.Login;
import com.android.wassally.networkUtils.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText ;
    private EditText mPasswordEditText ;
    private TextView mLoginErrorMessageTextView;
    private ProgressDialog progressDialog;

    private static final String AUTH_TOKEN ="auth_token";
    private static final String BASE_URL = "https://wassally.herokuapp.com/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = findViewById(R.id.login_email_et);
        mPasswordEditText = findViewById(R.id.login_password_et);
        TextView mSignUpTextView = findViewById(R.id.tv_sign_up);
        mLoginErrorMessageTextView = findViewById(R.id.tv_login_error_message);
        Button mLoginButton = findViewById(R.id.login_button);

        progressDialog = new ProgressDialog(this);


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
            }
        });
    }

    /**
     * send login request to the server using retrofit
     * @param login object consist of username or email and password
     */

    private void sendLoginRequest(Login login){

        // create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //get client and call object for the request
        UserClient client = retrofit.create(UserClient.class);
        Call<Login> loginCall = client.signIn(login);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call,@NonNull Response<Login> response) {
                progressDialog.cancel();

                if (response.body()!=null) {

                    String token = response.body().getToken();
                    String name = response.body().getName();

                    //save this token to sharedPreferences in order not to login every time when user lunch the app
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    preferences.edit().putString(AUTH_TOKEN, token).apply();
//                    @// TODO: 4/29/2019 get the user full name and save it in sharedPreferences instead of username
                    preferences.edit().putString("name", name).apply();

                    Toast.makeText(LoginActivity.this, "successful login", Toast.LENGTH_SHORT).show();

                    // start home activity
                    Intent loadHome = new Intent(LoginActivity.this, ClientHomeActivity.class);
                    startActivity(loadHome);
                    finish();
                }else {
                    //password or username is incorrect
                    mLoginErrorMessageTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

                progressDialog.cancel();
                Toast.makeText(LoginActivity.this,"Login Failed ",Toast.LENGTH_SHORT).show();

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
            progressDialog.setMessage("Signing in ..");
            progressDialog.show();

            Login login = new Login(email,password);
            sendLoginRequest(login);

        }
    }
}
