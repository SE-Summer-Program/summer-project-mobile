package com.sjtubus.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyDateUtils {

    /*
     * 获取昨天的开始时间。如2018-07-18调用，返回2018-07-17 00:00:00
     */
    public static Date getBeginOfDay(String datestr) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(datestr);
        } catch (Exception e){
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /* 同上，但返回 2018-07-17 */
    public static String getYesterdayStr(String datestr) {
//        Calendar cal = new GregorianCalendar();
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return StringCalendarUtils.CalendarToString(cal);
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(datestr));
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return StringCalendarUtils.CalendarToString(cal);
    }

    public static String getTomorrowStr(String datestr) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(datestr));
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return StringCalendarUtils.CalendarToString(cal);
    }

    /* 判断所给的日期是否在将来一周以内 */
    public static boolean isWithinOneWeek(String datestr){
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(StringCalendarUtils.getCurrrentDate()));
        cal.add(Calendar.WEEK_OF_MONTH, 1);
        String oneWeekLater = StringCalendarUtils.CalendarToString(cal);

        return StringCalendarUtils.isBeforeDateOfSecondPara(datestr, oneWeekLater);
    }

    public static boolean isWithinLastOneWeek(String datestr){
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(StringCalendarUtils.getCurrrentDate()));
        cal.add(Calendar.WEEK_OF_MONTH, -1);
        String oneWeekBefore = StringCalendarUtils.CalendarToString(cal);

        return StringCalendarUtils.isBeforeDateOfSecondPara(oneWeekBefore, datestr);
    }

    public static boolean isWithinLastOneMonth(String datestr){
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(StringCalendarUtils.getCurrrentDate()));
        cal.add(Calendar.MONTH, -1);
        String oneMonthBefore = StringCalendarUtils.CalendarToString(cal);

        return StringCalendarUtils.isBeforeDateOfSecondPara(oneMonthBefore, datestr);
    }

    public static boolean isWithinLastThreeMonth(String datestr){
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(StringCalendarUtils.getCurrrentDate()));
        cal.add(Calendar.MONTH, -3);
        String threeMonthBefore = StringCalendarUtils.CalendarToString(cal);

        return StringCalendarUtils.isBeforeDateOfSecondPara(threeMonthBefore, datestr);
    }
}
