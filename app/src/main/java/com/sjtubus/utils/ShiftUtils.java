package com.sjtubus.utils;

import java.util.Calendar;

import com.sjtubus.utils.StringCalendarUtils;

public class ShiftUtils {

    private static String[] line_list = {"闵行到徐汇", "徐汇到闵行", "闵行到七宝", "七宝到闵行"};
    private static String[] line_list_E = {"MinHangToXuHui", "XuHuiToMinHang", "MinHangToQiBao", "QiBaoToMinHang"};

    private static String[] type_list = {"在校期-工作日", "在校期-双休日、节假日", "寒暑假-工作日","寒暑假-双休日"};
    private static String[] type_list_E = {"NormalWorkday","NormalWeekendAndLegalHoliday","HolidayWorkday","HolidayWeekend"};

    public static String ERROR = "error";

    private StringCalendarUtils stringCalendarUtils;

    public static String getTypeByCalendar(Calendar calendar){
        //date = calendar.getTime();
        boolean isWeekendFlag = StringCalendarUtils.isWeekend(calendar);
        boolean isHoildayFlag = StringCalendarUtils.isHoilday(calendar);
        if (!isHoildayFlag && !isWeekendFlag){
            return type_list_E[0];
        }
        else if (!isHoildayFlag){
            return type_list_E[1];
        }
        else if (!isWeekendFlag){
            return type_list_E[2];
        }
        else{
            return type_list_E[3];
        }
    }

    public static String getLineByDepartureAndArrive(String departure_place_str, String arrive_place_str){
        if (departure_place_str.contains("闵行") || arrive_place_str.contains("徐汇")) {
            return line_list_E[0];
        }
        else if (departure_place_str.contains("徐汇") || arrive_place_str.contains("闵行")) {
            return line_list_E[1];
        }
        else if (departure_place_str.contains("闵行") || arrive_place_str.contains("七宝")) {
            return line_list_E[2];
        }
        else if (departure_place_str.contains("七宝") || arrive_place_str.contains("闵行")) {
            return line_list_E[3];
        }
        else {
            return ERROR;
        }
    }
}
