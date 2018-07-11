package com.sjtubus.widget;

import com.sjtubus.model.AppointDataBean;

public interface AppointItemClickListener {

    void onExpandChildItem(AppointDataBean appointDataBean);

    void onHideChildItem(AppointDataBean appointDataBean);

}