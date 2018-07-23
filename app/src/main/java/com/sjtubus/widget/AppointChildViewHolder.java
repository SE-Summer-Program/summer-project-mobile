package com.sjtubus.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.sjtubus.R;
import com.sjtubus.model.AppointInfo;

public class AppointChildViewHolder extends BaseViewHolder{

    private Context context;
    private View view;

    AppointChildViewHolder(Context context, View view){
        super(view);
        this.context = context;
        this.view = view;
    }

    public void bindView(final AppointInfo bean, final int pos, final View.OnClickListener listener){
        Button reserve_btn = view.findViewById(R.id.appointitem_reservebtn);
        Button collect_btn = view.findViewById(R.id.appointitem_collectbtn);
        Button info_btn = view.findViewById(R.id.appointitem_infobtn);
        reserve_btn.setTag(pos);
        reserve_btn.setOnClickListener(listener);
        collect_btn.setTag(pos);
        collect_btn.setOnClickListener(listener);
        info_btn.setTag(pos);
        info_btn.setOnClickListener(listener);
    }
}