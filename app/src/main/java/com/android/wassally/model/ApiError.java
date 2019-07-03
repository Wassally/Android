package com.android.wassally.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiError {
    @SerializedName("errors")
    private ArrayList<String> errors;

    @SerializedName("status_code")
    private int statusCode;

    public ArrayList<String> getErrors() {
        return errors;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
