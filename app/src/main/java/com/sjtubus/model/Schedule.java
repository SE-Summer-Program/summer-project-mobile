package com.sjtubus.model;

import java.util.ArrayList;

import java.util.List;

public class Schedule {

    private java.lang.String lineName;
        //MinToXu, XuToMin, MinToQi, QiToMin

    private java.lang.String types;
        //"NormalWorkday"，"NormalWeekendAndLegalHoilday"
        //"HoildayWorkday"，"HoildayWeekend"

    private java.lang.String subtitle;

    private List<java.lang.String> scheduleTime = new ArrayList<>();
    private List<java.lang.String> scheduleComment = new ArrayList<>();

    public Schedule(java.lang.String lineName, java.lang.String types){
        initScheduleTime(types);

        this.lineName = lineName;
        this.types = types;
        //this.scheduleTime = ... 根据linename和types，只添加满足情况的schedule

        this.subtitle = "首班车：8:00，末车班：20:00"; //获取schedule数组的头和尾
    }

    private void initScheduleTime(java.lang.String types){
        this.scheduleTime.add("8:00");
        this.scheduleTime.add("9:00");
        this.scheduleTime.add("20:00");
        this.scheduleComment.add("none");
        this.scheduleComment.add("none");
        this.scheduleComment.add("none");

        //在这里get不同类型的数据，并且赋值给scheduleTime
    }

    public java.lang.String getLineName(){
        return lineName;
    } //等同于getTitle

    public java.lang.String getTypes(){
        return types;
    }

    public java.lang.String getSubtitle() { return subtitle; }

    public List<java.lang.String> getSchedule(){ return scheduleTime; }

    public java.lang.String getDetail() {
        java.lang.String detail = "123";
        for (int i = 0; i < scheduleTime.size(); i++){
            detail.concat(scheduleTime.get(i));
            detail.concat(scheduleComment.get(i));
            detail.concat("\n");
        }
        return detail;
    } //实际应该使用getSchedule的，但是adapter那里传不进去list

}
