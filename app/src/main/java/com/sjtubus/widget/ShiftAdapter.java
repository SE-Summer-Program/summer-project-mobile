package com.sjtubus.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.Schedule;
import com.sjtubus.utils.StringCalendarUtils;

import java.util.ArrayList;
import java.util.List;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {
    private List<String> times;
    private List<String> comments;
    private Context context;

    private boolean isLoopLineFlag;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView shiftTime;
        TextView shiftComment;
        TextView shiftTimeLoop;

        ViewHolder(View view){
            super(view);
            shiftTime = (TextView) view.findViewById(R.id.shift_time);
            shiftComment = (TextView) view.findViewById(R.id.shift_comment);
            shiftTimeLoop = (TextView) view.findViewById(R.id.shift_time_loop);
        }
    }

    /* 校园巴士和校区巴士不共用一套setdatalist了 */
    public void setDataList(Schedule schedule) {
        times = schedule.getScheduleTime();
//        if (! isLoopLineFlag)
        comments = schedule.getScheduleComment();
        notifyDataSetChanged();
    }

    public void setDataListOfLoopLine(List<String> schedule){
        times = schedule;
        notifyDataSetChanged();
    }

    /* 校园巴士和校区巴士不共用一套view了 */
    public ShiftAdapter(Context context, boolean isLoopLineFlag){
        this.context = context;
        this.times = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.isLoopLineFlag = isLoopLineFlag;
    }

    @NonNull
    @Override
    public ShiftAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isLoopLineFlag){
            return new ShiftAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift_loop, parent, false));
        } else {
            return new ShiftAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift, parent, false));
        }
//        return new ShiftAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftAdapter.ViewHolder holder, int position) {
        if (isLoopLineFlag){
            String departure_time = StringCalendarUtils.HHmmssToHHmm(times.get(position));
            holder.shiftTimeLoop.setText(departure_time);
        } else {
            String departure_time = StringCalendarUtils.HHmmssToHHmm(times.get(position));
            holder.shiftTime.setText(departure_time);
            String comment = comments.get(position);
            holder.shiftComment.setText(comment);
        }
    }

    @Override
    public int getItemCount() {
        return times.size();
    }
}
