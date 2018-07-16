package com.sjtubus.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StringCalendarUtils {

//    private String datestr;
//
//    private Calendar calendar;

    public static Calendar StringToCalendar(String datestr){
        Calendar calendar = new GregorianCalendar();
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(datestr); //start_date是类似"2013-02-02"的字符串
            calendar.setTime(date);
        } catch (Exception e){
            e.printStackTrace();
        }
        return calendar;
    }

    public static Date StringToDate(String datestr){
        Date date= new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String DateToString(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 设置你想要的格式
        String dateStr = dateFormat.format(calendar.getTime());
        return dateStr;
    }

    public static boolean isWeekend(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return (day == Calendar.SATURDAY || day == Calendar.SUNDAY);
    }
    public static boolean isHoilday(Calendar calendar){
        int month = calendar.get(Calendar.MONTH);
        return (month == Calendar.FEBRUARY || month == Calendar.JUNE
                || month == Calendar.JULY);
    }

    public static String TimeToString(Calendar calendar){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");// 设置你想要的格式
        String timeStr = timeFormat.format(calendar.getTime());
        return timeStr;
    }
}
