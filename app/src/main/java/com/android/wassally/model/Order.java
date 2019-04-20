package com.android.wassally.model;

public class Order {
    private int id;
    private int ownerId;
    private int requestNumber;



    private String toPerson;
    private String toPlace;
    private String fromPlace;
    private String note;
    private int offer;
    private int weight;
    private int duration;

    public Order(int requestNumber, String toPerson, String toPlace, String fromPlace, String note, int offer, int weight, int duration) {
        this.requestNumber = requestNumber;
        this.toPerson = toPerson;
        this.toPlace = toPlace;
        this.fromPlace = fromPlace;
        this.note = note;
        this.offer = offer;
        this.weight = weight;
        this.duration = duration;
    }

    public Order(int id, int ownerId, String toPerson, String toPlace, String fromPlace, String note, int offer, int weight, int duration) {
        this.id = id;
        this.ownerId = ownerId;
        this.toPerson = toPerson;
        this.toPlace = toPlace;
        this.fromPlace = fromPlace;
        this.note = note;
        this.offer = offer;
        this.weight = weight;
        this.duration = duration;
    }

    public void setToPerson(String toPerson) {
        this.toPerson = toPerson;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getToPerson() {
        return toPerson;
    }

    public String getToPlace() {
        return toPlace;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public String getNote() {
        return note;
    }

    public int getOffer() {
        return offer;
    }

    public int getWeight() {
        return weight;
    }

    public int getDuration() {
        return duration;
    }
    public int getRequestNumber() {
        return requestNumber;
    }
}
