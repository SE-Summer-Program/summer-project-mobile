package com.sjtubus.utils;

import android.os.Bundle;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.sjtubus.R;
import com.sjtubus.model.Station;
import com.yinglan.scrolllayout.ScrollLayout;

public class MyMarkerClickListener implements BaiduMap.OnMarkerClickListener {
    private ScrollLayout mScrollLayout;

    public MyMarkerClickListener(ScrollLayout mScrollLayout){
        this.mScrollLayout = mScrollLayout;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //从marker中获取info信息
        Bundle bundle = marker.getExtraInfo();
        if(bundle == null) return false;
        Station station = (Station) bundle.getSerializable("info");
        //将信息显示在界面上
        TextView text = mScrollLayout.findViewById(R.id.map_station_name);
        String context = "";
        context += station.getName() + "\n";
        if(station.getName().equals("菁菁堂")){
            context += "逆时针：" + "\n";
            for(String time:station.getAntiClockLoop()){
                context += time + " ";
            }
            context += "\n";
            for(String time:station.getAntiClockNonLoop()){
                context += time + " ";
            }
            context += "\n" + "顺时针：" + "\n";
            for(String time:station.getClockLoop()){
                context += time + " ";
            }
            context += "\n";
            for(String time:station.getClockNonLoop()){
                context += time + " ";
            }
            //还有假期的四个时刻表没加
        }
        text.setText(context);
        mScrollLayout.setToOpen();
        return true;
    }
}
