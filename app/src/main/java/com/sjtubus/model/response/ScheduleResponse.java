package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.BusMessage;
import com.sjtubus.model.Schedule;

import java.util.List;

public class ScheduleResponse extends HttpResponse {
    @SerializedName("schedule")
    private Schedule schedule;

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
