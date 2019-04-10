package com.android.wassally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {




    //sending only user name (or email) and password
    @SerializedName("username")
    @Expose(deserialize = false)
    private String username;
    @SerializedName("password")
    @Expose(deserialize = false)
    private String password;

    //receive token and user id
    @SerializedName("auth_token")
    @Expose(serialize = false)
    private String token;
    @SerializedName("user_id")
    @Expose(serialize = false)
    private int id ;
    @SerializedName("email")
    @Expose(serialize = false)
    private String email;
    @SerializedName("name")
    @Expose(serialize = false)
    private String name ;

    public Login(String username, String password, String token, int id, String email, String name) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /** getter*/
    public String getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    /**setter*/

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
