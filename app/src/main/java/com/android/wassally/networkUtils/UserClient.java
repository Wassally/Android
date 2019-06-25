package com.android.wassally.networkUtils;

import com.android.wassally.model.Login;
import com.android.wassally.model.Order;
import com.android.wassally.model.SignUP;
import com.android.wassally.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
    @POST("accounts/")
   Call<User> createAccount(@Body SignUP signUP);

    @POST("login/")
    Call<Login> signIn (@Body Login login);

    @POST("packages/")
    Call<Order> createNewPackage (@Body Order order, @Header("Authorization") String authToken);
}
