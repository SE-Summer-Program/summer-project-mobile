package com.sjtubus.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.sjtubus.R;
import com.sjtubus.model.Appointment;

public class AppointChildViewHolder extends BaseViewHolder{

    private Context context;
    private View view;
    private Button status_btn;
    private Button like_btn;

    public AppointChildViewHolder(Context context, View view){
        super(view);
        this.context = context;
        this.view = view;
    }

    public void bindView(final Appointment bean, final int pos){
        status_btn = (Button)view.findViewById(R.id.appointitem_statusbtn);
        like_btn = (Button)view.findViewById(R.id.appointitem_likebtn);
    }
}