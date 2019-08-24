package com.android.wassally.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.wassally.model.Addresses.PackageAddress;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order implements Parcelable {

    // request
    @SerializedName("sender_phone_number")
    private String senderPhoneNumber;
    @SerializedName("receiver_name")
    private String receiverName;
    @SerializedName("receiver_phone_number")
    private String receiverPhoneNumber;
    @SerializedName("note")
    private String note;
    @SerializedName("duration")
    private int duration;
    @SerializedName("weight")
    private int weight;
    @SerializedName("transport_way")
    private String transportWay;
    @SerializedName("package_address")
    private PackageAddress packageAddress;

    // reply
    @SerializedName("id")
    @Expose(serialize = false)
    private int id;

    @SerializedName("owner")
    @Expose(serialize = false)
    private int ownerId;

    @SerializedName("delivery_state")
    @Expose(serialize = false)
    private String state;

    @SerializedName("created_at")
    @Expose(serialize = false)
    private String createdAt;

    @SerializedName("wassally_salary")
    @Expose(serialize = false)
    private int salary;

    @SerializedName("captain_name")
    @Expose(serialize = false)
    private String captainName;



    //constructor when sending request
    public Order(String senderPhoneNumber, String receiverName, String receiverPhoneNumber,
                 String note, int duration, int weight, String transportWay, PackageAddress packageAddress) {
        this.senderPhoneNumber = senderPhoneNumber;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.note = note;
        this.duration = duration;
        this.weight = weight;
        this.transportWay = transportWay;
        this.packageAddress = packageAddress;
    }

    public Order(String receiverName, String state, String createdAt) {
        this.receiverName = receiverName;
        this.state = state;
        this.createdAt = createdAt;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setTransportWay(String transportWay) {
        this.transportWay = transportWay;
    }

    public void setPackageAddress(PackageAddress packageAddress) {
        this.packageAddress = packageAddress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }


    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public String getNote() {
        return note;
    }

    public int getDuration() {
        return duration;
    }

    public int getWeight() {
        return weight;
    }

    public String getTransportWay() {
        return transportWay;
    }

    public PackageAddress getPackageAddress() {
        return packageAddress;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getState() {
        return state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getSalary() {
        return salary;
    }

    public String getCaptainName() {
        return captainName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(senderPhoneNumber);
        dest.writeString(receiverName);
        dest.writeString(receiverPhoneNumber);
        dest.writeString(note);
        dest.writeInt(duration);
        dest.writeInt(weight);
        dest.writeString(transportWay);
        dest.writeParcelable(packageAddress, flags);
        dest.writeInt(id);
        dest.writeInt(ownerId);
        dest.writeString(state);
        dest.writeString(createdAt);
        dest.writeInt(salary);
    }
    private Order(Parcel in) {
        senderPhoneNumber = in.readString();
        receiverName = in.readString();
        receiverPhoneNumber = in.readString();
        note = in.readString();
        duration = in.readInt();
        weight = in.readInt();
        transportWay = in.readString();
        packageAddress = in.readParcelable(PackageAddress.class.getClassLoader());
        id = in.readInt();
        ownerId = in.readInt();
        state = in.readString();
        createdAt = in.readString();
        salary = in.readInt();
    }


    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
