package com.android.wassally.networkUtils;

import com.android.wassally.model.ComputeSalary;
import com.android.wassally.model.Login;
import com.android.wassally.model.Order;
import com.android.wassally.model.SignUP;
import com.android.wassally.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserClient {
    @POST("accounts/")
    Call<User> createAccount(@Body SignUP signUP);

    @POST("login/")
    Call<Login> signIn(@Body Login login);

    @POST("packages/")
    Call<Order> createNewPackage(@Body Order order, @Header("Authorization") String authToken);

    @POST("computingsalary/")
    Call<ComputeSalary> getExpectedSalary(@Body ComputeSalary computeSalary);

    @GET("packages/")
    Call<List<Order>> getMyOrders(@Header("Authorization") String authToken);

    @GET("accounts/me/")
    Call<User> getUserInfo(@Header("Authorization") String authToken);

    @PUT("accounts/me/")
    Call<User> updateUserInfo(@Body User user,@Header("Authorization") String authToken);
}
