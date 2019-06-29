package com.android.wassally.model.Addresses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PackageAddress implements Parcelable {

    @SerializedName("to_address")
    private Address toAddress;
    @SerializedName("from_address")
    private Address fromAddress;


    public PackageAddress(Address toAddress, Address fromAddress) {
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
    }



    public void setToAddress(Address toAddress) {
        this.toAddress = toAddress;
    }

    public void setFromAddress(Address fromAddress) {
        this.fromAddress = fromAddress;
    }

    public Address getToAddress() {
        return toAddress;
    }

    public Address getFromAddress() {
        return fromAddress;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toAddress, flags);
        dest.writeParcelable(fromAddress, flags);
    }

    private PackageAddress(Parcel in) {
        toAddress = in.readParcelable(Address.class.getClassLoader());
        fromAddress = in.readParcelable(Address.class.getClassLoader());
    }

    public static final Creator<PackageAddress> CREATOR = new Creator<PackageAddress>() {
        @Override
        public PackageAddress createFromParcel(Parcel in) {
            return new PackageAddress(in);
        }

        @Override
        public PackageAddress[] newArray(int size) {
            return new PackageAddress[size];
        }
    };
}
