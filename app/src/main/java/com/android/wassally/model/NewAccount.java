package com.android.wassally.model;

import com.google.gson.annotations.SerializedName;

public class NewAccount extends User {

    @SerializedName("password")
    private String password;


    public NewAccount(String email, String username, String firstName, String lastName,
                      boolean isCaptain, boolean isClient, String governate, String city,
                      String phoneNumber, String[] captain, String image, String password) {

        super(email, username, firstName, lastName, isCaptain, isClient, governate, city, phoneNumber, captain, image);
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
