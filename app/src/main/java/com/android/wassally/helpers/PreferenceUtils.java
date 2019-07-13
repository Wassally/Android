package com.android.wassally.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.wassally.Constants;

public class PreferenceUtils {

    public static String getToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  "Token " + preferences.getString(Constants.AUTH_TOKEN, "");
    }

    public static String getFullName(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Constants.FULL_NAME, "");
    }

    public static void setFullName(String fullName,Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(Constants.FULL_NAME,fullName).apply();
    }

    public static void setToken(String token,Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(Constants.AUTH_TOKEN, token).apply();

    }
}
