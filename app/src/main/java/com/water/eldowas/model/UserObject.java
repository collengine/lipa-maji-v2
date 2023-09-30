package com.water.eldowas.model;

public class UserObject {

    public UserObject() {
    }

    private String userAccount;
    private String userRoute;
    private String userId;
    private String phoneNumber;

    public UserObject(String userAccount, String userRoute, String userId, String phoneNumber) {
        this.userAccount = userAccount;
        this.userRoute = userRoute;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
    }


    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserRoute() {
        return userRoute;
    }

    public void setUserRoute(String userRoute) {
        this.userRoute = userRoute;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "UserObject{" +
                "userAccount='" + userAccount + '\'' +
                ", userRoute='" + userRoute + '\'' +
                ", userId='" + userId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}


