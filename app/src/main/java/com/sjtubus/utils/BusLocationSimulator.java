package com.sjtubus.utils;

import com.baidu.mapapi.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusLocationSimulator {
    private Double totalTime = 1080d;//校车开一圈用时为18分钟
    private Double totalLength = 0d;//线路总长度
    public List<LatLng> points = new ArrayList<LatLng>();//存放所有途经点的位置
    private List<Double> distance = new ArrayList<Double>();//存放所有相邻距离
    private List<String> timeTable = new ArrayList<String>();
    private LatLng result = new LatLng(31.024766399515144,121.43630746466236);//初始点设为菁菁堂

    public BusLocationSimulator(){
        setData();
    }

    public LatLng getBusLocation(String time){
        //计算当前巴士已运行的时间
        SimpleDateFormat simpleFormat = new SimpleDateFormat("HH-mm-ss");
        Double s1 = -1d;//s1为较早发车的班次
        Double s2 = -1d;//s2不一定存在
        for(String tmp : timeTable){
            try {
                int s = (int) ((simpleFormat.parse(time).getTime() - simpleFormat.parse(tmp).getTime()) / 1000);
                if((s < totalTime) && (s > 0)){
                    if(s1 < 0) s1 = s * 1.0d;
                    else s2 = s * 1.0d;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //将已运行时间转化为已行驶路程
        Double length = s1 / totalTime * totalLength;
        //计算已经行驶到何处
        for(int i = 0; i < distance.size(); i++){
            if(length < distance.get(i)){
                Double latitude_tmp = (points.get(i + 1).latitude - points.get(i).latitude) * length / distance.get(i) + points.get(i).latitude;
                Double longitude_tmp = (points.get(i + 1).longitude - points.get(i).longitude) * length / distance.get(i) + points.get(i).longitude;
                result = new LatLng(latitude_tmp, longitude_tmp);
                break;
            }
            else length -= distance.get(i);
        }
        return result;
    }

    private void setData(){
        //载入途经点
        points.add(new LatLng(31.024766399515144,121.43630746466236));
        points.add(new LatLng(31.024959811937066,121.43699915990504));
        points.add(new LatLng(31.025209,121.437614));
        points.add(new LatLng(31.025499,121.43805));
        points.add(new LatLng(31.025592,121.438333));
        points.add(new LatLng(31.025658,121.438836));
        points.add(new LatLng(31.025864976800392,121.43990068670226));
        points.add(new LatLng(31.026064,121.440844));
        points.add(new LatLng(31.027466401048258,121.44552407919474));
        points.add(new LatLng(31.031628429647498,121.44371848511967));
        points.add(new LatLng(31.033098247158424,121.44829984322057));
        points.add(new LatLng(31.032547,121.4483));
        points.add(new LatLng(31.031615,121.448686));
        points.add(new LatLng(31.030996,121.449414));
        points.add(new LatLng(31.030684640009543,121.4510666241913));
        points.add(new LatLng(31.029361771422135,121.45170442110339));
        points.add(new LatLng(31.03035,121.454732));
        points.add(new LatLng(31.030559,121.454965));
        points.add(new LatLng(31.030872,121.455113));
        points.add(new LatLng(31.031294,121.455122));
        points.add(new LatLng(31.03251805877257,121.45457899873531));
        points.add(new LatLng(31.03502444699885,121.45342018462745));
        points.add(new LatLng(31.035908,121.452742));
        points.add(new LatLng(31.03682683932345,121.4513720480647));
        points.add(new LatLng(31.037332,121.449881));
        points.add(new LatLng(31.037252291712417,121.44823696183487));
        points.add(new LatLng(31.034390120473216,121.43949644922279));
        points.add(new LatLng(31.032208623507113,121.43292983594485));
        points.add(new LatLng(31.03071558408187,121.43357661591203));
        points.add(new LatLng(31.031705789035033,121.43656797326025));
        points.add(new LatLng(31.029214784878082,121.43755610932124));
        points.add(new LatLng(31.02816266397923,121.4344839044771));
        points.add(new LatLng(31.024766399515144,121.43630746466236));//终点与起点重合
        //计算每两个相邻点的距离及总长度
        for(int i = 0; i < points.size() - 1; i++){
            Double distance_tmp;
            distance_tmp = Math.sqrt(Math.pow(points.get(i).latitude - points.get(i + 1).latitude, 2)
                    + Math.pow(points.get(i).longitude - points.get(i + 1).longitude, 2));
            distance.add(distance_tmp);
            totalLength += distance_tmp;
        }
        //载入首发时刻
        timeTable.add("07-30-00");
        timeTable.add("07-45-00");
        timeTable.add("08-00-00");
        timeTable.add("08-15-00");
        timeTable.add("08-25-00");
        timeTable.add("08-40-00");
        timeTable.add("09-00-00");
        timeTable.add("09-20-00");
        timeTable.add("09-40-00");
        timeTable.add("10-00-00");
        timeTable.add("10-20-00");
        timeTable.add("10-40-00");
        timeTable.add("11-00-00");
        timeTable.add("11-20-00");
        timeTable.add("11-40-00");
        timeTable.add("12-00-00");
        timeTable.add("13-00-00");
        timeTable.add("13-20-00");
        timeTable.add("13-40-00");
        timeTable.add("14-00-00");
        timeTable.add("14-20-00");
        timeTable.add("14-40-00");
        timeTable.add("15-00-00");
        timeTable.add("15-20-00");
        timeTable.add("15-40-00");
        timeTable.add("16-00-00");
        timeTable.add("16-20-00");
        timeTable.add("16-30-00");
        timeTable.add("17-00-00");
        timeTable.add("17-15-00");
        timeTable.add("17-30-00");
        timeTable.add("17-50-00");
        timeTable.add("18-00-00");
        timeTable.add("19-00-00");
        timeTable.add("20-10-00");
    }
}
