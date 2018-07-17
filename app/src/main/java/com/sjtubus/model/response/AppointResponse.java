package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.AppointInfo;

import java.util.List;

public class AppointResponse extends HttpResponse {

    @SerializedName("appointInfos")
    private List<AppointInfo> appointInfoList;

    public List<AppointInfo> getAppointment() {
        return appointInfoList;
    }

    public void setAppointment(List<AppointInfo> appointInfoList) {
        this.appointInfoList = appointInfoList;
    }

}
