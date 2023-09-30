package com.water.eldowas.util;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by SONU on 27/03/16.
 */
public class Item_Model implements Serializable {

    /*  Model class for List and Recycler Items  */
    private String title, subTitle, index ,dbIndex;

    private String notificationTitle, notificationMessage, notificationTimestamp;
    private HashMap<String, Object> dateCreated;
    public Item_Model(String title, String subTitle, String index) {
        this.title = title;
        this.subTitle = subTitle;
        this.index =index;
        this.dbIndex =dbIndex;

    }

    public Item_Model(String title, String subTitle, String index, String dbIndex) {
        this.title = title;
        this.subTitle = subTitle;
        this.index =index;
        this.dbIndex =dbIndex;

    }

    public Item_Model( String notificationTitle, String notificationMessage, String notificationTimestamp, HashMap<String, Object> dateCreated) {

        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
        this.notificationTimestamp = notificationTimestamp;
        this.dateCreated = dateCreated;
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

    public HashMap<String, Object> getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(HashMap<String, Object> dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(String dbIndex) {
        this.dbIndex = dbIndex;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    @Override
    public String toString() {
        return "Item_Model{" +
                "title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", index='" + index + '\'' +
                ", dbIndex='" + dbIndex + '\'' +
                ", notificationTitle='" + notificationTitle + '\'' +
                ", notificationMessage='" + notificationMessage + '\'' +
                ", notificationTimestamp='" + notificationTimestamp + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
