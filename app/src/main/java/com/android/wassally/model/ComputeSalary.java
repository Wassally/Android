package com.android.wassally.model;

import com.android.wassally.model.Addresses.Location;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComputeSalary {
    @SerializedName("to_formated_address")
    @Expose(deserialize = false)
    private String toAddress;
    @SerializedName("from_formated_address")
    @Expose(deserialize = false)
    private String fromAddress;
    @SerializedName("to_location")
    @Expose(deserialize = false)
    private Location toLocation;
    @SerializedName("from_location")
    @Expose(deserialize = false)
    private Location fromLocation;
    @SerializedName("weight")
    @Expose(deserialize = false)
    private int weight;

    @SerializedName("expected_salary")
    @Expose(serialize = false)
    private double salary ;

    public ComputeSalary(String toAddress, String fromAddress, Location toLocation, Location fromLocation, int weight) {
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.toLocation = toLocation;
        this.fromLocation = fromLocation;
        this.weight = weight;
    }

    public ComputeSalary(int salary) {
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
