package com.water.eldowas.model;

public class UserAccount {

    public UserAccount() {
    }
    private String AccountNo;
    private String CustomerName;
    private String SubZone;

    public UserAccount(String accountNo, String customerName, String SubZone) {
        this.AccountNo = accountNo;
        this.CustomerName = customerName;
        this.SubZone = SubZone;
    }

    public String getSubZone() {
        return SubZone;
    }

    public void setSubZone(String subZone) {
        SubZone = subZone;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "AccountNo='" + AccountNo + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", SubZone='" + SubZone + '\'' +
                '}';
    }
}
