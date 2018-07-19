package com.sjtubus.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.sjtubus.R;
import com.sjtubus.model.Station;
import com.yinglan.scrolllayout.ScrollLayout;

import java.util.List;

public class MyMapStatusChangeListener implements BaiduMap.OnMapStatusChangeListener{
    private ScrollLayout mScrollLayout;
    private List<Marker> markers = null;
    private List<Station> stations = null;
    private Float zoom;

    public MyMapStatusChangeListener(ScrollLayout mScrollLayout){
        this.mScrollLayout = mScrollLayout;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public void setMarkers(List<Marker> markers) {
        this.markers = markers;
    }

    @Override
    public void onMapStatusChangeStart(MapStatus status){
        mScrollLayout.setToExit();
        this.zoom = status.zoom;
    }
    @Override
    public void onMapStatusChangeStart(MapStatus status,int reason){
    }

    @Override
    public void onMapStatusChange(MapStatus status){
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus status){
        /*Float zoom_new = status.zoom;
        if((zoom_new < 18.0f && this.zoom < 18.0f) ||
                (zoom_new >= 18.0f && this.zoom >= 18.0f))
                    return;
        BitmapDescriptor bd_temp ;
        View v_temp = LayoutInflater.from(getApplicationContext()).inflate(R.layout.map_marker, null);//加载自定义的布局
        ImageView img_temp = (ImageView) v_temp.findViewById(R.id.baidumap_custom_img);//获取自定义布局中的imageview
        img_temp.setImageResource(R.drawable.icon_gcoding);//设置marker的图标
        TextView tv_temp = (TextView) v_temp.findViewById(R.id.baidumap_custom_text);//获取自定义布局中的textview

        for(Station station : stations){
            if(zoom > 18.0f) tv_temp.setText(station.getName());//设置站点名
            else tv_temp.setText("");
            bd_temp = BitmapDescriptorFactory.fromView(v_temp);
            MarkerOptions marker_temp = new MarkerOptions()
                    .position(new LatLng(station.getLatitude(),station.getLongitude()))
                    .icon(bd_temp).anchor(0.5f, 0.5f).zIndex(7);
            //添加marker
            Marker marker = (Marker) mBaiduMap.addOverlay(marker_temp);
            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
            Bundle bundle = new Bundle();
            //info必须实现序列化接口
            bundle.putSerializable("info", station);
            marker.setExtraInfo(bundle);

            markers.add(marker);
        }
        zoomLevel = zoom;*/
    }
}


