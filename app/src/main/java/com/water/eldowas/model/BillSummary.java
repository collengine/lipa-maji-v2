package com.water.eldowas.model;

public class BillSummary {



    String BillNo;
    String AccountNo;
    String CurrentBillAmount;
    String AccBalance;
    String MeterNo;
    String PreviousReading;
    String CurrentReading;
    String Consumption;
    String BillType;
    String DateOfReading;
    String DueDate;
    String Payment;
    String BillingPeriod;


    public BillSummary(){
        
    }


    public BillSummary(String billNo, String accountNo, String currentBillAmount, String accBalance, String meterNo, String previousReading, String currentReading, String consumption, String billType, String dateOfReading, String dueDate, String payment, String billingPeriod) {
        BillNo = billNo;
        AccountNo = accountNo;
        CurrentBillAmount = currentBillAmount;
        AccBalance = accBalance;
        MeterNo = meterNo;
        PreviousReading = previousReading;
        CurrentReading = currentReading;
        Consumption = consumption;
        BillType = billType;
        DateOfReading = dateOfReading;
        DueDate = dueDate;
        Payment = payment;
        BillingPeriod = billingPeriod;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getCurrentBillAmount() {
        return CurrentBillAmount;
    }

    public void setCurrentBillAmount(String currentBillAmount) {
        CurrentBillAmount = currentBillAmount;
    }

    public String getAccBalance() {
        return AccBalance;
    }

    public void setAccBalance(String accBalance) {
        AccBalance = accBalance;
    }

    public String getMeterNo() {
        return MeterNo;
    }

    public void setMeterNo(String meterNo) {
        MeterNo = meterNo;
    }

    public String getPreviousReading() {
        return PreviousReading;
    }

    public void setPreviousReading(String previousReading) {
        PreviousReading = previousReading;
    }

    public String getCurrentReading() {
        return CurrentReading;
    }

    public void setCurrentReading(String currentReading) {
        CurrentReading = currentReading;
    }

    public String getConsumption() {
        return Consumption;
    }

    public void setConsumption(String consumption) {
        Consumption = consumption;
    }

    public String getBillType() {
        return BillType;
    }

    public void setBillType(String billType) {
        BillType = billType;
    }

    public String getDateOfReading() {
        return DateOfReading;
    }

    public void setDateOfReading(String dateOfReading) {
        DateOfReading = dateOfReading;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public String getBillingPeriod() {
        return BillingPeriod;
    }

    public void setBillingPeriod(String billingPeriod) {
        BillingPeriod = billingPeriod;
    }

    @Override
    public String toString() {
        return "BillSummary{" +
                "BillNo='" + BillNo + '\'' +
                ", AccountNo='" + AccountNo + '\'' +
                ", CurrentBillAmount='" + CurrentBillAmount + '\'' +
                ", AccBalance='" + AccBalance + '\'' +
                ", MeterNo='" + MeterNo + '\'' +
                ", PreviousReading='" + PreviousReading + '\'' +
                ", CurrentReading='" + CurrentReading + '\'' +
                ", Consumption='" + Consumption + '\'' +
                ", BillType='" + BillType + '\'' +
                ", DateOfReading='" + DateOfReading + '\'' +
                ", DueDate='" + DueDate + '\'' +
                ", Payment='" + Payment + '\'' +
                ", BillingPeriod='" + BillingPeriod + '\'' +
                '}';
    }
}
