package com.android.wassally.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.wassally.R;
import com.android.wassally.model.NewAccount;
import com.android.wassally.model.Profile;
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
    private EditText mPasswordEt;
    private EditText mPhoneNumberEt;
    private EditText mGovernateEt;
    private EditText mCityEt;
    private EditText mUserName;
    private Button mSignUpButton;

    // hardcoded values for testing  , remember to change user name on every test
    private String firstName = "Amr";
    private String lastName = "Anwar";
    private String email = "anwar@gmail.com";
    private String password = "5555";
    private Integer phoneNumber = 012523;
    private String governate = "Dumyat";
    private String username = "anwar";
    private String city = "Dumyat Al Jadidah";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        mFirstNameEt = findViewById(R.id.sign_up_first_name_et);
        mLastNameEt = findViewById(R.id.sign_up_last_name_et);
        mEmailEt = findViewById(R.id.sign_up_email_et);
        mPasswordEt = findViewById(R.id.sign_up_password_et);
        mPhoneNumberEt = findViewById(R.id.sign_up_phone_number_et);
        mGovernateEt = findViewById(R.id.sign_up_governate_et);
        mUserName = findViewById(R.id.sign_up_username_et);
        mCityEt = findViewById(R.id.sign_up_city_et);
        mSignUpButton = findViewById(R.id.sign_up_button);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* firstName = getText(mFirstNameEt);
                lastName = getText(mLastNameEt);
                email = getText(mEmailEt);
                password = getText(mPasswordEt);
                phoneNumber = getText(mPhoneNumberEt);
                username = getText(mUserName);
                governate = getText(mGovernateEt);
                city = getText(mCityEt);*/

                NewAccount newAccount = new NewAccount(email,username,firstName,lastName,false,
                        true,governate,city,phoneNumber,null,null,password,password);


                sendNetworkRequest(newAccount);

            }
        });
    }

    private String getText(View view) {
        String text;
        EditText editText = (EditText) view;
        text = editText.getText().toString();
        return text;
    }

    private void sendNetworkRequest(NewAccount newAccount){
        //create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://mahmoudzeyada.pythonanywhere.com/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        // get client and call object for the request
        UserClient client = retrofit.create(UserClient.class);
        Call<Profile> call = client.createAccount(newAccount);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                assert response.body() != null;
                Toast.makeText(SignUpActivity.this,"yeah! UserId: "+response.body().getId(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(SignUpActivity.this,"some thing went wrong! :( ",Toast.LENGTH_SHORT).show();

            }
        });

    }

}
