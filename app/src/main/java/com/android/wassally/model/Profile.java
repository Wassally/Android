package com.android.wassally.model;

import com.google.gson.annotations.SerializedName;

public class Profile extends User {
    @SerializedName("id")
    private Integer id;

    @SerializedName("auth_token")
    private String token;

    public Profile(String email, String username, String firstName, String lastName,
                   boolean isCaptain, boolean isClient, String governate,
                   String city, String phoneNumber, String[] captain, String image, int id,String token) {
        super(email, username, firstName, lastName, isCaptain, isClient, governate, city, phoneNumber, captain, image);
        this.id = id;
        this.token =token;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
