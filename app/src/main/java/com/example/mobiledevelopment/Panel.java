package com.example.mobiledevelopment;

import com.google.gson.annotations.SerializedName;

public class Panel {

    @SerializedName("panel_type")
    public String panelType;
    @SerializedName("power")
    public int power;
    @SerializedName("battery_capacity")
    public int batteryCapacity;
    @SerializedName("address")
    public String address;
    @SerializedName("image")
    public String image;

    public Panel(String panelType, int power, String address, int batteryCapacity, String image) {
        this.panelType = panelType;
        this.power = power;
        this.address = address;
        this.batteryCapacity = batteryCapacity;
        this.image = image;
    }

    public Panel(String panelType, String power, String address, String batteryCapacity) {
        this.panelType = panelType;
        this.power = Integer.parseInt(power);
        this.address = address;
        this.batteryCapacity = Integer.parseInt(batteryCapacity);
        this.image = "https://fsmedia.imgix.net/4f/2a/c7/1f/6e01/41ff/8fa5/9774418130fc.jpeg?crop=edges&fit=crop&auto=format%2Ccompress&dpr=2&h=325&w=650";
    }

}
