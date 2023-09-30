package com.water.eldowas.model;

public class AccountObject {

    public AccountObject() {
    }
    private String subTitle;
    private String AccountNumber;
    private String AccountRoute;
    private String AccountPhoneNumber;
    private String userId;
    private String accountIndex;

    public AccountObject(String subTitle, String accountNumber, String accountRoute, String accountPhoneNumber, String userId) {
        this.subTitle = subTitle;
        AccountNumber = accountNumber;
        AccountRoute = accountRoute;
        AccountPhoneNumber = accountPhoneNumber;
        this.userId = userId;
    }
    public AccountObject(String subTitle, String accountNumber, String accountRoute, String accountPhoneNumber, String userId, String accountIndex) {
        this.subTitle = subTitle;
        AccountNumber = accountNumber;
        AccountRoute = accountRoute;
        AccountPhoneNumber = accountPhoneNumber;
        this.userId = userId;
        this.accountIndex = accountIndex;
    }

    public String getAccountIndex() {
        return accountIndex;
    }

    public void setAccountIndex(String accountIndex) {
        this.accountIndex = accountIndex;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getAccountRoute() {
        return AccountRoute;
    }

    public void setAccountRoute(String accountRoute) {
        AccountRoute = accountRoute;
    }

    public String getAccountPhoneNumber() {
        return AccountPhoneNumber;
    }

    public void setAccountPhoneNumber(String accountPhoneNumber) {
        AccountPhoneNumber = accountPhoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
