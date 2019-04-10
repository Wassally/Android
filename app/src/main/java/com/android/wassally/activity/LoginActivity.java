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
import com.android.wassally.model.Login;
import com.android.wassally.networkUtils.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mLoginButton;
    private TextView mSignUpView;
    private TextView mLoginErrorMessageTextView;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.email_sign_in_button);
        mSignUpView = findViewById(R.id.tv_sign_up);
        progressDialog = new ProgressDialog(this);
        mLoginErrorMessageTextView = findViewById(R.id.tv_login_error_message);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mSignUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendLoginRequest(Login login){
        // create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://mahmoudzeyada.pythonanywhere.com/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =builder.build();
        //get client and call object for the request
        UserClient client = retrofit.create(UserClient.class);
        Call<Login> loginCall = client.signIn(login);
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call,@NonNull Response<Login> response) {
                progressDialog.cancel();
                assert response.body() != null;

                String token = response.body().getToken();
                //save this token to sharedPreferences in order not to login every time user lunch the app
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                preferences.edit().putString("auth_token",token).apply();

                Toast.makeText(LoginActivity.this,"successful login",Toast.LENGTH_SHORT).show();


                // start home activity
                Intent loadHome = new Intent(LoginActivity.this, ClientHomeActivity.class);
                startActivity(loadHome);
                finish();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

                progressDialog.cancel();
                Toast.makeText(LoginActivity.this,"Login Failed ",Toast.LENGTH_SHORT).show();
                mLoginErrorMessageTextView.setVisibility(View.VISIBLE);

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
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email =mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        //check for a valid password if the user entered one
        if (TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if(!TextUtils.isEmpty(password)&&!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
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

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
