package com.sjtubus.model;

import com.sjtubus.R;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    public List<Schedule> mScheduleList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView scheduleTitle;
        TextView scheduleSubTitle;
        TextView scheduleDetail;

        public ViewHolder(View view){
            super(view);
            scheduleTitle = (TextView) view.findViewById(R.id.schedule_title);
            scheduleSubTitle = (TextView) view.findViewById(R.id.schedule_subtitle);
            scheduleDetail = (TextView) view.findViewById(R.id.schedule_detail);
        }
    }

    public ScheduleAdapter(List<Schedule> scheduleList){
        mScheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = mScheduleList.get(position);
        holder.scheduleTitle.setText(schedule.getLineName());
        holder.scheduleSubTitle.setText(schedule.getSubtitle());
        holder.scheduleDetail.setText(schedule.getDetail()); //这个不对的
    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }
}
