package com.sjtubus.utils;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;

import com.yinglan.scrolllayout.ScrollLayout;

import com.sjtubus.model.Station;
import com.sjtubus.R;

public class MyMarkerClickListener implements BaiduMap.OnMarkerClickListener {
    private ScrollLayout mScrollLayout;

    public MyMarkerClickListener(ScrollLayout mScrollLayout){
        this.mScrollLayout = mScrollLayout;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //从marker中获取info信息
        Bundle bundle = marker.getExtraInfo();
        Station station = (Station) bundle.getSerializable("info");
        //将信息显示在界面上
        TextView text = (TextView) mScrollLayout.findViewById(R.id.text_view);
        String context = "";
        context += station.getName() + "\n";
        if(station.getName() == "菁菁堂"){
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
