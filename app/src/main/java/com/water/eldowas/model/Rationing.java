package com.water.eldowas.model;

public class Rationing {

    private String places;
    private String day;
    private String time;
    private String description;
    private String datePosted;

    public Rationing() {
    }

    public Rationing(String places, String day, String time, String description, String datePosted) {
        this.places = places;
        this.day = day;
        this.time = time;
        this.description = description;
        this.datePosted = datePosted;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    @Override
    public String toString() {
        return "Rationing{" +
                "places='" + places + '\'' +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", datePosted='" + datePosted + '\'' +
                '}';
    }
}
