package com.sjtubus.model;

import android.util.Log;
import android.widget.Toast;

import com.sjtubus.activity.ScheduleActivity;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private String lineName;
        //MinToXu, XuToMin, MinToQi, QiToMin

    private String types;
        //"NormalWorkday"，"NormalWeekendAndLegalHoilday"
        //"HoildayWorkday"，"HoildayWeekend"

    private String subtitle;

    private List<String> scheduleTime = new ArrayList<>();

    public Schedule(String lineName){
       // Log.d("model schedule:", types);
        this.lineName = lineName;
        this.types = "None";
        //this.scheduleTime = ... 根据linename和types，只添加满足情况的schedule
        initScheduleTime();
        this.subtitle = "首班车：8:00，末车班：20:00"; //获取schedule数组的头和尾
    }

    private void initScheduleTime(){
        scheduleTime.add("8:00");
        scheduleTime.add("9:00");
        scheduleTime.add("20:00");
    }

    public String getLineName(){
        return lineName;
    } //等同于getTitle

    public String getTypes(){
        return types;
    }

    public String getSubtitle() { return subtitle; }

    public List<String> getSchedule(){ return scheduleTime; }

    public String getDetail() { return "8:00\n9:00\n"; } //实际应该使用getSchedule的，但是adapter那里传不进去list

}
