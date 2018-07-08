package com.sjtubus.widget;

import com.sjtubus.R;
import com.sjtubus.activity.LineActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.ViewHolder>{

    private List<String> mLineList;
    private LineActivity context;
    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView linename;
        TextView scheduleSubTitle;
        TextView scheduleDetail;

        public ViewHolder(View view){
            super(view);
            scheduleSubTitle = (TextView) view.findViewById(R.id.schedule_subtitle);
            linename = (TextView) view.findViewById(R.id.schedule_title);
      //      scheduleSubTitle = (TextView) view.findViewById(R.id.schedule_subtitle);
            scheduleDetail = (TextView) view.findViewById(R.id.schedule_detail);
        }
    }

    public void setDataList(List<String> list) {
        mLineList = list;
        notifyDataSetChanged();
    }

    public LineAdapter(LineActivity context){
        this.context = context;
        this.mLineList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String line_name = mLineList.get(position);
        holder.linename.setText(line_name);
    }

    public String getLinename(int index){
        return mLineList.get(index);
    }

    @Override
    public int getItemCount() {
        return mLineList.size();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view);
    }
}
