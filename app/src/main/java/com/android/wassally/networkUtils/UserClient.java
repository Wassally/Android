package com.android.wassally.networkUtils;

import com.android.wassally.model.NewAccount;
import com.android.wassally.model.Profile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserClient {
    @POST("accounts/")
   Call<Profile> createAccount(@Body NewAccount newAccount);
}
