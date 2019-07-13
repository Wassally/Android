package com.android.wassally.helpers;

import com.android.wassally.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    public static Retrofit createRetrofitInstance (){
        // create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }
}
