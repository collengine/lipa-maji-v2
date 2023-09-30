package com.water.eldowas.model;

import java.util.HashMap;
import com.google.firebase.database.Exclude;

/**
 * Created by sulu on 24/06/2018.
 */

public class Reciept {
    private HashMap<String, Object> dateCreated;
    private String month;
    private String mpesaRecieptNumber;
    private String amount;
    private String timeStamp;

    public Reciept() {
    }

    public Reciept(String month, String mpesaRecieptNumber, String amount,HashMap<String, Object> dateCreated) {
        this.month = month;
        this.mpesaRecieptNumber = mpesaRecieptNumber;
        this.amount = amount;
        this.dateCreated = dateCreated;
    }

    public HashMap<String, Object> getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(HashMap<String, Object> dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMpesaRecieptNumber() {
        return mpesaRecieptNumber;
    }

    public void setMpesaRecieptNumber(String mpesaRecieptNumber) {
        this.mpesaRecieptNumber = mpesaRecieptNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }



    @Exclude
    public long getDateCreatedLong(){
        if (this.dateCreated != null) {
            return (long)this.dateCreated.get("date");
            //        return dateCreated;
        }else{
            long num = 1519327135445L;
            return num;
        }
//        HashMap<String,Object> dateCreateObj= new HashMap<>();
//        dateCreateObj.put("date", ServerValue.TIMESTAMP);
//        return (long)dateCreateObj.get("date");
    }
}
