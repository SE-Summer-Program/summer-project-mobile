package com.sjtubus.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

import com.sjtubus.R;
import com.sjtubus.model.Appointment;

public class AppointParentViewHolder extends BaseViewHolder{

    private Context context;
    private View view;
    private LinearLayout container;
    private View parentDashedView;

    private TextView shiftid;
    private TextView departure_place;
    private TextView arrive_place;
    private TextView departure_time;
    private TextView arrive_time;
    private TextView remain_seat;
    private ImageView expand;

    public AppointParentViewHolder(Context context, View view){
        super(view);
        this.context = context;
        this.view = view;
    }

    public void bindView(final Appointment bean, final int pos, final AppointItemClickListener appointItemClickListener){
        container = (LinearLayout) view.findViewById(R.id.container);

        shiftid = (TextView)view.findViewById(R.id.appointitem_shiftid);
        departure_place = (TextView)view.findViewById(R.id.appointitem_departureplace);
        arrive_place = (TextView)view.findViewById(R.id.appointitem_arriveplace);
        departure_time = (TextView)view.findViewById(R.id.appointitem_departuretime);
        arrive_time = (TextView)view.findViewById(R.id.appointitem_arrivetime);
        remain_seat = (TextView)view.findViewById(R.id.appointitem_remainseat);
        expand = (ImageView)view.findViewById(R.id.appointitem_expand);

       // parentDashedView = view.findViewById(R.id.parent_dashed_view);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)expand.getLayoutParams();
        expand.setLayoutParams(params);

        shiftid.setText(bean.getShiftid());
        departure_place.setText(bean.getDeparture_place());
        arrive_place.setText(bean.getArrive_place());
        departure_time.setText(bean.getDeparture_time());
        arrive_time.setText(bean.getArrive_time());
      //  remain_seat.setText(((String) bean.getRemain_seat()));

        if (bean.isExpand()) {
            expand.setRotation(90);
          //  parentDashedView.setVisibility(View.INVISIBLE);
        } else {
            expand.setRotation(0);
          //  parentDashedView.setVisibility(View.VISIBLE);
        }

        //父布局OnClick监听
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appointItemClickListener != null){
                    if (bean.isExpand()) {
                        appointItemClickListener.onHideChildItem(bean);
                      //  parentDashedView.setVisibility(View.VISIBLE);
                        bean.setExpand(false);
                        rotationExpandIcon(90, 0);
                    } else {
                        appointItemClickListener.onExpandChildItem(bean);
                      //  parentDashedView.setVisibility(View.INVISIBLE);
                        bean.setExpand(true);
                        rotationExpandIcon(0, 90);
                    }
                }
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon(float from, float to) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    expand.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }
}