package com.sjtubus.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StringCalendarUtils {

    /* 由 2018-07-17 格式的字符串获取 calendar */
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

    /* calendar转化成 2018-07-18 的格式 */
    public static String CalendarToString(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 设置你想要的格式
        String dateStr = dateFormat.format(calendar.getTime());
        return dateStr;
    }

    /* calendar转化成 2018-07-18 19:35:00 的格式 */
    public static String CalendarToStringTime(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置你想要的格式
        String dateStr = dateFormat.format(calendar.getTime());
        return dateStr;
    }

    /* 将date类型转化为 格式 2018-07-18 的string */
    public static String DateToString(Date date){
//        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String date_str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_str = sdf.format(date);
        return date_str;
    }

    public static Date CalendarToDate(Calendar calendar){
        return calendar.getTime();
    }


    /*************************************************************************/


    /* 删取字符串的秒部分 */
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

    /* 获取当天的日期，格式 2018-07-18 */
    public static String getCurrrentDate(){
        String current_date="";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        current_date=simpleDateFormat.format(date);
        return current_date;
    }

    /* 获取当天的时间 格式 2018-07-18 19:35:00 */
    public static String getCurrentTime(){
        String current_time="";
        Date date = new Date();
        //最后的aa表示“上午”或“下午” HH表示24小时制  如果换成hh表示12小时制
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        current_time=simpleDateFormat.format(date);
        return current_time;
    }

    /* 让月份保持为两位的字符串 */
    public static String getDoubleDigitMonth(int month){
        String monthstr;
        if (month + 1 < 10){
            int m = month + 1;
            monthstr = "0" + m;
        }
        else
            monthstr = "" + month;
        return monthstr;
    }

    /* 让日期保持为两位的字符串 */
    public static String getDoubleDigitDay(int day){
        String daystr;
        if (day + 1 < 10){
            daystr = "0" + day;
        }
        else
            daystr = "" + day;
        return daystr;
    }


    /*************************************************************************/


    /* 由calendar判断是否是双休日 */
    public static boolean isWeekend(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return (day == Calendar.SATURDAY || day == Calendar.SUNDAY);
    }
    /* 由calendar判断是否是节假日 是否是二月，七月，八月 */
    public static boolean isHoilday(Calendar calendar){
        int month = calendar.get(Calendar.MONTH);
        return (month == Calendar.FEBRUARY || month == Calendar.AUGUST
                || month == Calendar.JULY);
    }

    /**
     * @description: 比较某个时间和现在时间的先后
     */
    public static boolean isBeforeCurrentTime(String datetime) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Date current = new Date();

        try {
            date = timeFormat.parse(datetime);
            current = timeFormat.parse(timeFormat.format(new Date()));
        } catch (Exception e){
            e.printStackTrace();
        }
        if (date.before(current))
            return true;
        else
            return false;
    }

    /**
     * @description: 比较某个日期和现在日期的先后
     */
    public static boolean isBeforeCurrentDate(String datestr){
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date current = new Date();
        try {
            date = timeFormat.parse(datestr);
            current = timeFormat.parse(timeFormat.format(new Date()));
        } catch (Exception e){
            e.printStackTrace();
        }
        if (date.before(current))
            return true;
        else
            return false;
    }

    /* 判断所给的时间字符串是否是今天 */
    public static boolean isToday(String datestr){
        if (datestr.equals(getCurrrentDate()))
            return true;
        else
            return false;
    }
}
