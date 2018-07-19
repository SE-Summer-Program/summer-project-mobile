package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Station  implements Serializable{
    @SerializedName("station")
    private String name;//站点中文名
    @SerializedName("longitude")
    private Double longitude;//经度
    @SerializedName("latitude")
    private Double latitude;//纬度
    //V = Vactation
    //A = AntiClockwise
    //C = Clockwise
    //L = Loopline
    //N = Nonloopline
    @SerializedName("AntiClockLoop")
    private List<String> AntiClockLoop = null;
    @SerializedName("AntiClockNonLoop")
    private List<String> AntiClockNonLoop = null;
    @SerializedName("ClockLoop")
    private List<String> ClockLoop = null;
    @SerializedName("ClockNonLoop")
    private List<String> ClockNonLoop = null;
    @SerializedName("VacAntiClockLoop")
    private List<String> VacAntiClockLoop = null;
    @SerializedName("VacAntiClockNonLoop")
    private List<String> VacAntiClockNonLoop = null;
    @SerializedName("VacClockLoop")
    private List<String> VacClockLoop = null;
    @SerializedName("VacClockNonLoop")
    private List<String> VacClockNonLoop = null;

    public Station(String name, Double latitude, Double longitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public String getName(){
        return this.name;
    }
    public Double getLongitude(){
        return this.longitude;
    }
    public Double getLatitude() {
        return this.latitude;
    }

    public void setAntiClockLoop(List<String> AntiClockLoop) {
        this.AntiClockLoop = AntiClockLoop;
    }
    public void setAntiClockNonLoop(List<String> AntiClockNonLoop) {
        this.AntiClockNonLoop = AntiClockNonLoop;
    }
    public void setClockLoop(List<String> ClockLoop) {
        this.ClockLoop = ClockLoop;
    }
    public void setClockNonLoop(List<String> ClockNonLoop) {
        this.ClockNonLoop = ClockNonLoop;
    }
    public void setVacAntiClockLoop(List<String> VacAntiClockLoop) {
        this.VacAntiClockLoop = VacAntiClockLoop;
    }
    public void setVacAntiClockNonLoop(List<String> VacAntiClockNonLoop) {
        this.VacAntiClockNonLoop = VacAntiClockNonLoop;
    }
    public void setVacClockLoop(List<String> VacClockLoop) {
        this.VacClockLoop = VacClockLoop;
    }
    public void setVacClockNonLoop(List<String> VacClockNonLoop) {
        this.VacClockNonLoop = VacClockNonLoop;
    }
    public List<String> getAntiClockLoop() {
        return this.AntiClockLoop;
    }
    public List<String> getAntiClockNonLoop() {
        return this.AntiClockNonLoop;
    }
    public List<String> getClockLoop() {
        return this.ClockLoop;
    }
    public List<String> getClockNonLoop() {
        return this.ClockNonLoop;
    }
    public List<String> getVacAntiClockLoop() {
        return this.VacAntiClockLoop;
    }
    public List<String> getVacAntiClockNonLoop() {
        return this.VacAntiClockNonLoop;
    }
    public List<String> getVacClockLoop() {
        return this.VacClockLoop;
    }
    public List<String> getVacClockNonLoop() {
        return this.VacClockNonLoop;
    }
}
