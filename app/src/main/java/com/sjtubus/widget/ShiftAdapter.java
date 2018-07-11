package com.sjtubus.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.BusMessage;
import com.sjtubus.model.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {
    private List<String> times;
    private List<String> comments;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView shiftTime;
        TextView shiftComment;

        ViewHolder(View view){
            super(view);
            shiftTime = (TextView) view.findViewById(R.id.shift_time);
            shiftComment = (TextView) view.findViewById(R.id.shift_comment);
        }
    }

    public void setDataList(Schedule schedule) {
        times = schedule.getScheduleTime();
        comments = schedule.getScheduleComment();
        notifyDataSetChanged();
    }

    public ShiftAdapter(Context context){
        this.context = context;
        this.times = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    @NonNull
    @Override
    public ShiftAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift, parent, false);
        ShiftAdapter.ViewHolder holder = new ShiftAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftAdapter.ViewHolder holder, int position) {
        String departure_time = times.get(position);
        String comment = comments.get(position);
        holder.shiftTime.setText(departure_time);
        holder.shiftComment.setText(comment);

    }

    @Override
    public int getItemCount() {
        return times.size();
    }
}
