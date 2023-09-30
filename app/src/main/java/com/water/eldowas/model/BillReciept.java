package com.water.eldowas.model;

public class BillReciept {


    private  String AccountNo;
    private  String CustomerName;
    private  String BillNo;
    private  String BillingPeriod;
    private  String MeterNo;
    private  String CurrentReading;
    private  String PreviousReading;
    private  String Consumption;
    private  String DateOfReading;
    private  String WaterAmount;
    private  String SewerAmount;
    private  String RentAmount;
    private  String CurrentBillAmount;
    private  String BillType;
    private  String DueDate;
    private  String AccBalance;



    public BillReciept(){

    }

    public BillReciept(String accountNo, String customerName, String billNo, String billingPeriod, String meterNo, String currentReading, String previousReading, String consumption, String dateOfReading, String waterAmount, String sewerAmount, String rentAmount, String currentBillAmount, String billType, String dueDate,String accBalance) {
        AccountNo = accountNo;
        CustomerName = customerName;
        BillNo = billNo;
        BillingPeriod = billingPeriod;
        MeterNo = meterNo;
        CurrentReading = currentReading;
        PreviousReading = previousReading;
        Consumption = consumption;
        DateOfReading = dateOfReading;
        WaterAmount = waterAmount;
        SewerAmount = sewerAmount;
        RentAmount = rentAmount;
        CurrentBillAmount = currentBillAmount;
        BillType = billType;
        DueDate = dueDate;
        AccBalance = accBalance;
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

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getBillingPeriod() {
        return BillingPeriod;
    }

    public void setBillingPeriod(String billingPeriod) {
        BillingPeriod = billingPeriod;
    }

    public String getMeterNo() {
        return MeterNo;
    }

    public void setMeterNo(String meterNo) {
        MeterNo = meterNo;
    }

    public String getCurrentReading() {
        return CurrentReading;
    }

    public void setCurrentReading(String currentReading) {
        CurrentReading = currentReading;
    }

    public String getPreviousReading() {
        return PreviousReading;
    }

    public void setPreviousReading(String previousReading) {
        PreviousReading = previousReading;
    }

    public String getConsumption() {
        return Consumption;
    }

    public void setConsumption(String consumption) {
        Consumption = consumption;
    }

    public String getDateOfReading() {
        /*String[] date = DateOfReading.split(" ");
        String dateOnly = date[0];
        String[] separateDate = dateOnly.split("-");
        String month = getMonth(separateDate[0]);
        String datefinal = separateDate[1];
        String year = separateDate[2];
        String finalDate = datefinal + " " + month + ", " + year;
        return finalDate;*/
        return DateOfReading;

    }

    public void setDateOfReading(String dateOfReading) {
        DateOfReading = dateOfReading;
    }

    public String getWaterAmount() {
        return WaterAmount;
    }

    public void setWaterAmount(String waterAmount) {
        WaterAmount = waterAmount;
    }

    public String getSewerAmount() {
        return SewerAmount;
    }

    public void setSewerAmount(String sewerAmount) {
        SewerAmount = sewerAmount;
    }

    public String getRentAmount() {
        return RentAmount;
    }

    public void setRentAmount(String rentAmount) {
        RentAmount = rentAmount;
    }

    public String getCurrentBillAmount() {
        return CurrentBillAmount;
    }

    public void setCurrentBillAmount(String currentBillAmount) {
        CurrentBillAmount = currentBillAmount;
    }

    public String getBillType() {
        return BillType;
    }

    public void setBillType(String billType) {
        BillType = billType;
    }

    public String getDueDate() {

        /*    String[] date = DueDate.split(" ");
            String dateOnly = date[0];
            String[] separateDate = dateOnly.split("-");
            String month = getMonth(separateDate[0]);
            String datefinal = separateDate[1];
            String year = separateDate[2];
            String finalDate = datefinal + " " + month + ", " + year;
            return finalDate;*/
        return DueDate;

    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getAccBalance() {
        return AccBalance;
    }

    public void setAccBalance(String accBalance) {
        AccBalance = accBalance;
    }

    @Override
    public String toString() {
        return "BillReciept{" +
                "AccountNo='" + AccountNo + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", BillNo='" + BillNo + '\'' +
                ", BillingPeriod='" + BillingPeriod + '\'' +
                ", MeterNo='" + MeterNo + '\'' +
                ", CurrentReading='" + CurrentReading + '\'' +
                ", PreviousReading='" + PreviousReading + '\'' +
                ", Consumption='" + Consumption + '\'' +
                ", DateOfReading='" + DateOfReading + '\'' +
                ", WaterAmount='" + WaterAmount + '\'' +
                ", SewerAmount='" + SewerAmount + '\'' +
                ", RentAmount='" + RentAmount + '\'' +
                ", CurrentBillAmount='" + CurrentBillAmount + '\'' +
                ", BillType='" + BillType + '\'' +
                ", DueDate='" + DueDate + '\'' +
                ", AccBalance='" + AccBalance + '\'' +
                '}';
    }
    /*   @Override
    public String toString() {
        return "BillReciept{" +
                "AccountNo='" + AccountNo + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", BillNo='" + BillNo + '\'' +
                ", BillingPeriod='" + BillingPeriod + '\'' +
                ", MeterNo='" + MeterNo + '\'' +
                ", CurrentReading='" + CurrentReading + '\'' +
                ", PreviousReading='" + PreviousReading + '\'' +
                ", Consumption='" + Consumption + '\'' +
                ", DateOfReading='" + DateOfReading + '\'' +
                ", WaterAmount='" + WaterAmount + '\'' +
                ", SewerAmount='" + SewerAmount + '\'' +
                ", RentAmount='" + RentAmount + '\'' +
                ", CurrentBillAmount='" + CurrentBillAmount + '\'' +
                ", BillType='" + BillType + '\'' +
                ", DueDate='" + DueDate + '\'' +
                '}';
    }*/

    private String getMonth(String month){
        String mMonth = "";

        if(month.equalsIgnoreCase("1")){
            mMonth = "January";
        }else if(month.equalsIgnoreCase("2")){
            mMonth = "February";
        }else if(month.equalsIgnoreCase("3")){
            mMonth = "March";
        }else if(month.equalsIgnoreCase("4")){
            mMonth = "April";
        }else if(month.equalsIgnoreCase("5")){
            mMonth = "May";
        }else if(month.equalsIgnoreCase("6")){
            mMonth = "June";
        }else if(month.equalsIgnoreCase("7")){
            mMonth = "July";
        }else if(month.equalsIgnoreCase("8")){
            mMonth = "August";
        }else if(month.equalsIgnoreCase("9")){
            mMonth = "September";
        }else if(month.equalsIgnoreCase("10")){
            mMonth = "October";
        }else if(month.equalsIgnoreCase("11")){
            mMonth = "November";
        }else if(month.equalsIgnoreCase("12")){
            mMonth = "December";
        }

        return mMonth;
    }
}
