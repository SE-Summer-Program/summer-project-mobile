package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.Appointment;

import java.util.List;

public class AppointResponse extends HttpResponse {

    @SerializedName("appointment")
    private List<Appointment> appointmentList;

    public List<Appointment> getAppointment() {
        return appointmentList;
    }

    public void setAppointment(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

}
