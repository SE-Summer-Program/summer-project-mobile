package com.sjtubus.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.sjtubus.R;

public class StationTimeLineViewHolder extends RecyclerView.ViewHolder{
    private TextView stationName;

    StationTimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        TimelineView mTimelineView = itemView.findViewById(R.id.time_marker);
        stationName = itemView.findViewById(R.id.station_name);
        mTimelineView.initLine(viewType);
    }

    public void setData(String name){
        stationName.setText(name);
    }
}
