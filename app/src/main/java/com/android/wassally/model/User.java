package com.android.wassally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * this is the super class of user model, contains the main common fields
 * **/

public class User {
    @SerializedName ("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName ("is_captain")
    private boolean isCaptain;
    @SerializedName ("is_client")
    private boolean isClient;
    @SerializedName("governate")
    private String governate;
    @SerializedName("city")
    private String city;
    @SerializedName ("phone_number")
    private Integer phoneNumber;
    @SerializedName("captain")
    @Expose(serialize = false, deserialize = false)
    private String [] captain;
    @SerializedName("image")
    @Expose(serialize = false, deserialize = false)
    private String image;

    //constructor

    public User(String email, String username, String firstName, String lastName,
                boolean isCaptain, boolean isClient, String governate, String city,
                Integer phoneNumber, String[] captain, String image) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isCaptain = isCaptain;
        this.isClient = isClient;
        this.governate = governate;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.captain = captain;
        this.image = image;
    }

    /** setter **/

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCaptain(boolean captain) {
        isCaptain = captain;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public void setGovernate(String governate) {
        this.governate = governate;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCaptain(String[] captain) {
        this.captain = captain;
    }

    public void setImage(String image) {
        this.image = image;
    }


    /** getter **/

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isCaptain() {
        return isCaptain;
    }

    public boolean isClient() {
        return isClient;
    }

    public String getGovernate() {
        return governate;
    }

    public String getCity() {
        return city;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public String[] getCaptain() {
        return captain;
    }

    public String getImage() {
        return image;
    }
}
