package com.sjtubus.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StringCalendarUtils {

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

    public static String HHmmssToHHmm(String timestr){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        try {
            date = dateFormat.parse(timestr);
        } catch (Exception e){
            e.printStackTrace();
        }
        return dateFormat2.format(date);
    }

    public static String CalendarToString(Calendar calendar){
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

    public static String getCurrrentDate(){
        String current_date="";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        current_date=simpleDateFormat.format(date);
        return current_date;
    }

    public static String getCurrentTime(){
        String current_time="";
        Date date = new Date();
        //最后的aa表示“上午”或“下午” HH表示24小时制  如果换成hh表示12小时制
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        current_time=simpleDateFormat.format(date);
        return current_time;
    }

    public static String DateToString(Date date){
//        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String date_str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_str = sdf.format(date);
        return date_str;
    }

    public static String HHmmssToHHmm(String timestr){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        try {
            date = dateFormat.parse(timestr);
        } catch (Exception e){
            e.printStackTrace();
        }
        return dateFormat2.format(date);
    }
}
