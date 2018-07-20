package com.sjtubus.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.activity.RecordActivity;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.ZxingUtils;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

    private List<RecordInfo> recordInfos = new ArrayList<>();
    private RecordActivity context;
    private RecordAdapter.OnItemClickListener mItemClickListener;

    public void setItemClickListener(RecordAdapter.OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setDataList(List<RecordInfo> recordInfos) {
        this.recordInfos = recordInfos;
        notifyDataSetChanged();
    }

    public RecordAdapter(RecordActivity context){
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView submittime;
        TextView linename;
        TextView departuremsg;
        TextView shiftid;
        TextView status;
        Button detailbtn;
        ImageView qrcode;

        ViewHolder(View view){
            super(view);
            submittime = view.findViewById(R.id.record_submittime);
            linename = view.findViewById(R.id.record_linename);
            departuremsg = view.findViewById(R.id.record_departuremsg);
            shiftid = view.findViewById(R.id.record_shiftid);
            status = view.findViewById(R.id.record_status);
            detailbtn = view.findViewById(R.id.record_btn);
            qrcode = view.findViewById(R.id.record_qrcode);
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
        String confirm_time = recordInfos.get(position).getConfirmDate();
        String line_name = recordInfos.get(position).getLineName();
        String departure_msg = recordInfos.get(position).getDepartureMsg();
        String shift_id = recordInfos.get(position).getShiftid();
        String submit_time = "预定时间： " + recordInfos.get(position).getSubmiTime();
        holder.submittime.setText(confirm_time);
        holder.linename.setText(line_name);
        holder.departuremsg.setText(departure_msg);
        holder.shiftid.setText(shift_id);
        holder.submittime.setText(submit_time);
        String info = shift_id + ";" + recordInfos.get(position).getDepartureDate() + ";"
                + UserManager.getInstance().getUser().getUsername();
        holder.qrcode.setImageBitmap(ZxingUtils.createQRImage(info,300,300));
    }

    @Override
    public int getItemCount(){
        return recordInfos.size();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view);
    }

//    private View.OnClickListener ChildListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v){
//            switch (v.getId()){
//                case R.id.record_detailbtn:
//                    ToastUtils.showShort("详细信息功能还不能使用哦~");
//                    break;
//            }
//        }
//    };
}
