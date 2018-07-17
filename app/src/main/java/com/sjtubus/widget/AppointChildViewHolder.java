package com.sjtubus.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.sjtubus.R;
import com.sjtubus.model.AppointInfo;

public class AppointChildViewHolder extends BaseViewHolder{

    private Context context;
    private View view;
    private Button reserve_btn;
    private Button info_btn;

    public AppointChildViewHolder(Context context, View view){
        super(view);
        this.context = context;
        this.view = view;
    }

    public void bindView(final AppointInfo bean, final int pos, final View.OnClickListener listener){
        reserve_btn = view.findViewById(R.id.appointitem_reservebtn);
        info_btn = view.findViewById(R.id.appointitem_infobtn);
        reserve_btn.setTag(pos);
        reserve_btn.setOnClickListener(listener);
        info_btn.setTag(pos);
        info_btn.setOnClickListener(listener);
    }
}