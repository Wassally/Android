package com.android.wassally.model.Addresses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Address implements Parcelable {
    @SerializedName("location")
    private Location location;
    @SerializedName("formated_address")
    private String formatedAddress;
    @SerializedName("address_description")
    private String description;

    public Address(Location location, String formatedAddress, String description) {
        this.location = location;
        this.formatedAddress = formatedAddress;
        this.description = description;
    }



    public void setLocation(Location location) {
        this.location = location;
    }

    public void setFormatedAddress(String formatedAddress) {
        this.formatedAddress = formatedAddress;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public String getFormatedAddress() {
        return formatedAddress;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(location, flags);
        dest.writeString(formatedAddress);
        dest.writeString(description);
    }

    private Address(Parcel in) {
        location = in.readParcelable(Location.class.getClassLoader());
        formatedAddress = in.readString();
        description = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
