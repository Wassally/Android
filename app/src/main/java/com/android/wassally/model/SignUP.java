package com.android.wassally.model;

import com.google.gson.annotations.SerializedName;

public class SignUP  {

    @SerializedName("first_name")
    private String FirstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("is_client")
    private boolean isClient;

    @SerializedName("is_captain")
    private boolean isCaptain;

    public SignUP(String firstName, String lastName, String email, String userName,
                  String password, String phoneNumber, boolean isClient, boolean isCaptain) {
        FirstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isClient = isClient;
        this.isCaptain = isCaptain;
    }


}
