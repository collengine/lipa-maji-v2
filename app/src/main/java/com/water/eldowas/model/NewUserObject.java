package com.water.eldowas.model;

public class NewUserObject {


    private String phoneNumber;
    private String gmail;
    private String userName;
    private String state;



    public NewUserObject() {
    }

    public NewUserObject(String phoneNumber, String gmail, String userName, String state) {
        this.phoneNumber = phoneNumber;
        this.gmail = gmail;
        this.userName = userName;
        this.state = state;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    @Override
    public String toString() {
        return "NewUserObject{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", gmail='" + gmail + '\'' +
                ", userName='" + userName + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
