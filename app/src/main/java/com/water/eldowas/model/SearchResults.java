package com.water.eldowas.model;

public class SearchResults {

    private String account;
    private String name;
    private String plot;
    private String meter_serial;
    public String subzone;

    public SearchResults(){

    }

    public SearchResults(String account, String name, String plotNumber, String meterNumber, String lastReading, String subzone) {
        this.account = account;
        this.name = name;
        this.plot = plotNumber;
        this.meter_serial = meterNumber;
        this.subzone = subzone;
    }

    public String getSubzone() {
        return subzone;
    }

    public void setSubzone(String subzone) {
        this.subzone = subzone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlotNumber() {
        return plot;
    }

    public void setPlotNumber(String plotNumber) {
        this.plot = plotNumber;
    }

    public String getMeterNumber() {
        return meter_serial;
    }

    public void setMeterNumber(String meterNumber) {
        this.meter_serial = meterNumber;
    }



    @Override
    public String toString() {
        return "SearchResults{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", plotNumber='" + plot + '\'' +
                ", meterNumber='" + meter_serial + '\'' +
                ", subzone='" + subzone + '\'' +
                '}';
    }
}
