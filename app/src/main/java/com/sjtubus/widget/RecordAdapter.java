package com.sjtubus.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.activity.RecordActivity;
import com.sjtubus.model.RecordInfo;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

    private List<RecordInfo> recordInfos;
    private RecordActivity context;
    private RecordAdapter.OnItemClickListener mItemClickListener;

    public void setItemClickListener(RecordAdapter.OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public RecordAdapter(RecordActivity context){
        this.context = context;
        this.recordInfos = new ArrayList<>();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

//        TextView linename;
//        TextView firsttime;
//        TextView lasttime;

        public ViewHolder(View view){
            super(view);
//            linename = (TextView) view.findViewById(R.id.line_name);
//            firsttime = (TextView) view.findViewById(R.id.line_first_time);
//            lasttime = (TextView) view.findViewById(R.id.line_last_time);
        }
    }

    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        final RecordAdapter.ViewHolder holder = new RecordAdapter.ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return recordInfos.size();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view);
    }
}
