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
    public String img;

    public Panel(String panelType, int power, String address, int batteryCapacity, String img) {
        this.panelType = panelType;
        this.power = power;
        this.address = address;
        this.img = img;
        this.batteryCapacity = batteryCapacity;
    }

}
