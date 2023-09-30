package com.water.eldowas.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class NotificationModel {

    public NotificationModel() {
    }

    private String notificationTitle, notificationMessage, notificationTimestamp, accountIndex;
    private HashMap<String, Object> dateCreated;
    private HashMap<String, Object> dateLastChanged;

    public NotificationModel(String notificationTitle, String notificationMessage, String notificationTimestamp,HashMap<String, Object> dateCreated) {
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
        this.notificationTimestamp = notificationTimestamp;
        this.dateCreated = dateCreated;
    }

    public NotificationModel(String notificationTitle, String notificationMessage, String notificationTimestamp,   HashMap<String, Object> dateLastChanged, String accountIndex) {
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
        this.notificationTimestamp = notificationTimestamp;
        this.accountIndex = accountIndex;
        this.dateLastChanged = dateLastChanged;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationTimestamp() {
        return notificationTimestamp;
    }

    public void setNotificationTimestamp(String notificationTimestamp) {
        this.notificationTimestamp = notificationTimestamp;
    }



    @Exclude
    public long getDateLastChangedLomg(){
        return (long)dateLastChanged.get("date");
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

    public HashMap<String, Object> getDateLastChanged() {
        return dateLastChanged;
    }

    public void setDateLastChanged(HashMap<String, Object> dateLastChanged) {
        this.dateLastChanged = dateLastChanged;
    }

    public HashMap<String,Object> getDateCreated(){
        if (dateCreated!=null) {
            return dateCreated;
        }
        HashMap<String,Object> dateCreateObj= new HashMap<>();
        dateCreateObj.put("date", ServerValue.TIMESTAMP);
        return dateCreateObj;
    }


    public String getAccountIndex() {
        return accountIndex;
    }

    public void setAccountIndex(String accountIndex) {
        this.accountIndex = accountIndex;
    }

    public void setDateCreated(HashMap<String, Object> dateCreated) {
        this.dateCreated = dateCreated;
    }


    @Override
    public String toString() {
        return "NotificationModel{" +
                "notificationTitle='" + notificationTitle + '\'' +
                ", notificationMessage='" + notificationMessage + '\'' +
                ", notificationTimestamp='" + notificationTimestamp + '\'' +
                ", accountIndex='" + accountIndex + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateLastChanged=" + dateLastChanged +
                '}';
    }
}
