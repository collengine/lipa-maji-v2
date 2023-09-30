package com.water.eldowas.model;

/**
 * Created by sulu on 06/08/2017.
 */
/**
 * Created by sulu on 22/07/2017.
 */

public class UserInformation {
    public String userName ;
    public String status ;
    public String photoUrl;
    public String uid;
    public String userGroup;
    public String firstName ;
    public String lastName ;
    public String county;
    public String town;
    public String bio;
    public String wallpaper;
    public String link;
    public int Id;
    public String state;
    private String verified;

    //needed by firebase
    public UserInformation (){
    }


    public UserInformation(String uid, String userName, String status, String photoUrl,String wallpaper,  String userGroup, String firstName, String lastName,String town, String county, String bio, String link, String state, String verified) {
        this.userName = userName;
        this.status = status;
        this.photoUrl = photoUrl;
        this.uid = uid;
        this.userGroup = userGroup;
        this.firstName = firstName;
        this.lastName = lastName;
        this.county = county;
        this.bio = bio;
        this.town = town;
        this.wallpaper = wallpaper;
        this.link = link;
        this.state = state;
        this.verified = verified;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(String wallpaper) {
        this.wallpaper = wallpaper;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
