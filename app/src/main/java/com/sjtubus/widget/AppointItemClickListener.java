package com.sjtubus.widget;

import com.sjtubus.model.Appointment;

public interface AppointItemClickListener {

    void onExpandChildItem(Appointment appointment);

    void onHideChildItem(Appointment appointment);

}