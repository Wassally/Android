package com.android.wassally.model;

import com.google.gson.annotations.SerializedName;

public class NewAccount extends User {

    @SerializedName("password")
    private String password;
    @SerializedName("confirm_password")
    private String confirmPassword;

    public NewAccount(String email, String username, String firstName, String lastName,
                      boolean isCaptain, boolean isClient, String governate, String city,
                      Integer phoneNumber, String[] captain, String image, String password, String confirmPassword) {
        super(email, username, firstName, lastName, isCaptain, isClient, governate, city, phoneNumber, captain, image);
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
