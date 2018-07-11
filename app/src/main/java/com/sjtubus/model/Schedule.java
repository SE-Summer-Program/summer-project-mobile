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
    private List<String> scheduleComment = new ArrayList<>();

    public Schedule(String lineName, String types){
       // initScheduleTime(types);

        this.lineName = lineName;
        this.types = types;
        //this.scheduleTime = ... 根据linename和types，只添加满足情况的schedule

        this.subtitle = "首班车：8:00，末车班：20:00"; //获取schedule数组的头和尾
    }

    private void initScheduleTime(String types){
        this.scheduleTime.add("8:00");
        this.scheduleTime.add("9:00");
        this.scheduleTime.add("20:00");
        this.scheduleComment.add("none");
        this.scheduleComment.add("none");
        this.scheduleComment.add("none");



        //在这里get不同类型的数据，并且赋值给scheduleTime
    }

    public String getLineName(){
        return lineName;
    } //等同于getTitle

    public String getTypes(){
        return types;
    }

    public String getSubtitle() { return subtitle; }

    public List<String> getSchedule(){ return scheduleTime; }

    public String getDetail() {
        scheduleTime.add("8:00");
        scheduleTime.add("9:00");
        scheduleTime.add("20:00");

        this.scheduleComment.add("none");
        this.scheduleComment.add("none");
        this.scheduleComment.add("none");

        String detail = "123";
        for (String item:scheduleTime){
            detail.concat(item);
            detail.concat("\n");
        }
        return detail;
    } //实际应该使用getSchedule的，但是adapter那里传不进去list

}
