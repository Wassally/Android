package com.android.wassally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    // request
    @SerializedName("from_govrnate")
    private String fromGovernate;
    @SerializedName("from_city")
    private String fromCity;
    @SerializedName("from_address")
    private String fromAddress;
    @SerializedName("sender_phone_number")
    private String senderPhoneNumber;
    @SerializedName("to_governate")
    private String toGovernate;
    @SerializedName("to_city")
    private String toCity;
    @SerializedName("to_address")
    private String toAddress;
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

    // reply
    @SerializedName("id")
    @Expose(serialize = false)
    private int id;

    @SerializedName("owner")
    @Expose(serialize = false)
    private int ownerId;

    @SerializedName("state")
    @Expose(serialize = false)
    private String state;

    public Order(String fromGovernate, String fromCity, String fromAddress,String senderPhoneNumber,
                 String toGovernate, String toCity, String toAddress, String receiverName,
                 String receiverPhoneNumber, String note, int duration, int weight, String transportWay) {

        this.fromGovernate = fromGovernate;
        this.fromCity = fromCity;
        this.fromAddress = fromAddress;
        this.toGovernate = toGovernate;
        this.toCity = toCity;
        this.toAddress = toAddress;
        this.receiverName = receiverName;
        this.senderPhoneNumber = senderPhoneNumber;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.note = note;
        this.duration = duration;
        this.weight = weight;
        this.transportWay = transportWay;
    }

    public Order(String fromGovernate, String fromCity, String fromAddress, String senderPhoneNumber,
                 String toGovernate, String toCity, String toAddress, String receiverName,
                 String receiverPhoneNumber, String note, int duration, int weight,
                 String transportWay, int id, int ownerId, String state) {
        this.fromGovernate = fromGovernate;
        this.fromCity = fromCity;
        this.fromAddress = fromAddress;
        this.senderPhoneNumber = senderPhoneNumber;
        this.toGovernate = toGovernate;
        this.toCity = toCity;
        this.toAddress = toAddress;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.note = note;
        this.duration = duration;
        this.weight = weight;
        this.transportWay = transportWay;
        this.id = id;
        this.ownerId = ownerId;
        this.state = state;
    }

    public void setFromGovernate(String fromGovernate) {
        this.fromGovernate = fromGovernate;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public void setToGovernate(String toGovernate) {
        this.toGovernate = toGovernate;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFromGovernate() {
        return fromGovernate;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToGovernate() {
        return toGovernate;
    }

    public String getToCity() {
        return toCity;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
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

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getState() {
        return state;
    }

}
