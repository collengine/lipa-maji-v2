package com.water.eldowas.model;

/**
 * Created by sulu on 25/11/2017.
 */

public class Contacts {



    private String id;
    private String uid;
    private String firstName;
    private String lastName;
    private String meterNumber;
    private String plotNumber;
    private String meterName;
    private String index;
    private String phoneNumber;
    private String subZone;


    public Contacts() {
    }

    public Contacts(String id, String uid, String firstName, String lastName, String meterNumber, String plotNumber, String meterName, String index) {
        this.id = id;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.meterNumber = meterNumber;
        this.plotNumber = plotNumber;
        this.meterName = meterName;
        this.index = index;
    }

    public Contacts(String id, String uid, String firstName, String lastName, String meterNumber, String plotNumber, String meterName) {
        this.id = id;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.meterNumber = meterNumber;
        this.plotNumber = plotNumber;
        this.meterName = meterName;
    }

    public Contacts(String id, String uid, String firstName, String lastName, String meterNumber, String plotNumber, String meterName, String index, String phoneNumber, String subZone) {
        this.id = id;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.meterNumber = meterNumber;
        this.plotNumber = plotNumber;
        this.meterName = meterName;
        this.index = index;
        this.phoneNumber = phoneNumber;
        this.subZone = subZone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSubZone() {
        return subZone;
    }

    public void setSubZone(String subZone) {
        this.subZone = subZone;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getPlotNumber() {
        return plotNumber;
    }

    public void setPlotNumber(String plotNumber) {
        this.plotNumber = plotNumber;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", meterNumber='" + meterNumber + '\'' +
                ", plotNumber='" + plotNumber + '\'' +
                ", meterName='" + meterName + '\'' +
                ", index='" + index + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", subZone='" + subZone + '\'' +
                '}';
    }
}
