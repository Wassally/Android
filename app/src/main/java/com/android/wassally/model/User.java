package com.android.wassally.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.wassally.model.Addresses.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User implements Parcelable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("auth_token")
    private String token;
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
    @SerializedName ("phone_number")
    private String phoneNumber;
    @SerializedName("user_addresses")
    private List<Address> addresses;
    @SerializedName("image")
    private String image;

    public User(String email, String username, String firstName, String lastName, String phoneNumber, String image) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.image = image;
    }

    public User(String email, String username, String firstName, String lastName, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    /** setter **/

    public void setId(Integer id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /** getter **/

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    private User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        token = in.readString();
        email = in.readString();
        username = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        isCaptain = in.readByte() != 0;
        isClient = in.readByte() != 0;
        phoneNumber = in.readString();
        addresses = in.createTypedArrayList(Address.CREATOR);
        image = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(token);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeByte((byte) (isCaptain ? 1 : 0));
        dest.writeByte((byte) (isClient ? 1 : 0));
        dest.writeString(phoneNumber);
        dest.writeTypedList(addresses);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
