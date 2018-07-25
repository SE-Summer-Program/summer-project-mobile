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

        if (bean.getRemain_seat() == 0){
            reserve_btn.setBackgroundColor(0xffbfbfbf);
            //0xFFBFBFBF是int类型的数据，分组一下0x|FF|BFBFBF，
            //0x是代表颜色整数的标记，ff是表示透明度，bfbfbf表示颜色，(primary_gray)
            //注意：这里0xFFBFBFBF必须是8个的颜色表示，不接受BFBFBF这种6个的颜色表示。
            reserve_btn.setText("无座");
        }
    }
}