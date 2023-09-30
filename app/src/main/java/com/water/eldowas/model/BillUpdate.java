package com.water.eldowas.model;

public class BillUpdate {


    private String AccountNo;
    private String AccBalance;
    private String LastUpdated;

    public BillUpdate(String accountNo, String accBalance, String lastUpdated) {
        AccountNo = accountNo;
        AccBalance = accBalance;
        LastUpdated = lastUpdated;
    }

    public BillUpdate(){

    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getAccBalance() {
        return AccBalance;
    }

    public void setAccBalance(String accBalance) {
        AccBalance = accBalance;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "BillUpdate{" +
                "AccountNo='" + AccountNo + '\'' +
                ", AccBalance='" + AccBalance + '\'' +
                ", LastUpdated='" + LastUpdated + '\'' +
                '}';
    }
}
