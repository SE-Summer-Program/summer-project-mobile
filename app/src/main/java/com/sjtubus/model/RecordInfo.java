package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author wxw
 * @date 2018/7/18 10:09
 * */

public class RecordInfo {

    private String confirmDate; //提交预约申请的时间

    @SerializedName("lineName")
    private String lineName;

    @SerializedName("departureDate")
    private String departureDate;
    @SerializedName("departureTime")
    private String departureTime; //预约的日期和时间
    @SerializedName("shiftid")
    private String shiftid;

    @SerializedName("status")
    private String status;

    private String departureMsg; //根据item_record的格式来的

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getShiftid() {
        return shiftid;
    }

    public void setShiftid(String shiftid) {
        this.shiftid = shiftid;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getDepartureMsg() {
        departureMsg = getDepartureDate() + " " + getDepartureTime() + getShiftid();
        return departureMsg;
    }

    public void setDepartureMsg(String departureMsg) {
        this.departureMsg = departureMsg;
    }
}
