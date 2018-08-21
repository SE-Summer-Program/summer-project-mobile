package com.sjtubus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.ShiftUtils;
import com.sjtubus.utils.StringCalendarUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class OrderActivity extends BaseActivity implements View.OnClickListener{

    private String departure_place_str, arrive_place_str;
    private String departure_time_str, arrive_time_str;
    private String date_str;
    private String shiftid_str;
    private String shift_type_str;

    private String double_departure_place_str, double_arrive_place_str;
    private String double_departure_time_str, double_arrive_time_str;
    private String double_date_str;
    private String double_shiftid_str;
    private String double_shift_type_str;

    private ImageView remind_icon, need_icon;
    private LinearLayout remind_time, remind_phone, remind_mail;
    private LinearLayout need_front, need_back, need_window, need_other;

    private EditText comment1, comment2, comment3, comment4;
    private ImageView comment1_clear, comment2_clear, comment3_clear, comment4_clear;

    private boolean isRemindExtend, isNeedExtend;
    private String[] remind_list = {"提前10分钟","提前30分钟","提前1小时","提前2小时","不提醒"};
    private String[] phone_location_list ={"大陆地区", "港澳台地区", "国外地区"};
    private boolean[] checked_array = {false, false, false, false};
    private String[] check_msg = {"前排座位", "后排座位", "靠窗座位", "其他特殊要求"};

    private int SINGLE_WAY = 0, DOUBLE_WAY = 1;
    private boolean isSingleWayFlag = true;
    private boolean isOrderFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPassData();
        initToolbar();
        initRemindView();
        initNeedView();
        initExtendFlag();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order;
    }

    private void initPassData(){
        // 单程 到 order
        Intent intent = getIntent();
        departure_place_str = intent.getStringExtra("departure_place");
        arrive_place_str = intent.getStringExtra("arrive_place");
        departure_time_str = intent.getStringExtra("departure_time");
        arrive_time_str = intent.getStringExtra("arrive_time");
        date_str = intent.getStringExtra("departure_date");
        shiftid_str = intent.getStringExtra("shiftid");
        shift_type_str = intent.getStringExtra("shift_type");

        int appoint_type = intent.getIntExtra("appoint_type",0);

        // 第二页 到 order
        if (appoint_type == DOUBLE_WAY){
            double_departure_place_str = intent.getStringExtra("double_departure_place");
            double_arrive_place_str = intent.getStringExtra("double_arrive_place");
            double_departure_time_str = intent.getStringExtra("double_departure_time");
            double_arrive_time_str = intent.getStringExtra("double_arrive_time");
            double_date_str = intent.getStringExtra("double_departure_date");
            double_shiftid_str = intent.getStringExtra("double_shiftid");
            double_shift_type_str = intent.getStringExtra("double_shift_type");

            isSingleWayFlag = false;
        }
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar_order);
        mToolbar.setNavigationIcon(R.drawable.back_32);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // order 回 单程
                if (isSingleWayFlag)
                    finish();
                // order 回 第二页
                else{
                    Intent appointDoubleIntent = new Intent(OrderActivity.this, AppointDoubleActivity.class);

                    appointDoubleIntent.putExtra("single_departure_place", departure_place_str);
                    appointDoubleIntent.putExtra("single_arrive_place", arrive_place_str);
                    appointDoubleIntent.putExtra("single_departure_time", departure_time_str);
                    //ToastUtils.showShort(StringCalendarUtils.HHmmssToHHmm(departure_time));
                    appointDoubleIntent.putExtra("single_arrive_time", arrive_time_str);
                    appointDoubleIntent.putExtra("single_departure_date", date_str);
                    appointDoubleIntent.putExtra("single_shiftid", shiftid_str);
                    appointDoubleIntent.putExtra("single_shift_type", shift_type_str);

                    appointDoubleIntent.putExtra("double_departure_date", double_date_str);
                    appointDoubleIntent.putExtra("target_page", 1);

//                    startActivity(appointDoubleIntent);
                    setResult(RESULT_OK, appointDoubleIntent);
                    finish();
                }
               // finish();
            }
        });

        initShiftInfo();
    }

    private void initShiftInfo() {
        TextView departure_place = findViewById(R.id.order_departureplace);
        TextView departure_time = findViewById(R.id.order_departuretime);
        TextView departure_date = findViewById(R.id.order_departuredate);
        TextView arrive_place = findViewById(R.id.order_arriveplace);
        TextView arrive_time = findViewById(R.id.order_arrivetime);
        TextView arrive_date = findViewById(R.id.order_arrivedate);
        TextView shiftid = findViewById(R.id.order_shiftid);
        TextView shift_type = findViewById(R.id.order_shifttype);
        TextView comment = findViewById(R.id.order_comment);

        departure_place.setText(departure_place_str);
        arrive_place.setText(arrive_place_str);
        departure_time.setText(departure_time_str);
        arrive_time.setText(arrive_time_str);
        departure_date.setText(date_str);
        arrive_date.setText(date_str);
        shiftid.setText(shiftid_str);
        shift_type.setText(ShiftUtils.getChiType(shift_type_str));
        comment.setOnClickListener(this);

        LinearLayout linearLayout = findViewById(R.id.order_linearlayout_double);

        if (isSingleWayFlag){
            linearLayout.setVisibility(View.GONE);
        } else {
            TextView double_departure_place = findViewById(R.id.order_departureplace_double);
            TextView double_departure_time = findViewById(R.id.order_departuretime_double);
            TextView double_departure_date = findViewById(R.id.order_departuredate_double);
            TextView double_arrive_place = findViewById(R.id.order_arriveplace_double);
            TextView double_arrive_time = findViewById(R.id.order_arrivetime_double);
            TextView double_arrive_date = findViewById(R.id.order_arrivedate_double);
            TextView double_shiftid = findViewById(R.id.order_shiftid_double);
            TextView double_shift_type = findViewById(R.id.order_shifttype_double);
            TextView double_comment = findViewById(R.id.order_comment_double);

            double_departure_place.setText(double_departure_place_str);
            double_arrive_place.setText(double_arrive_place_str);
            double_departure_time.setText(double_departure_time_str);
            double_arrive_time.setText(double_arrive_time_str);
            double_departure_date.setText(double_date_str);
            double_arrive_date.setText(double_date_str);
            double_shiftid.setText(double_shiftid_str);
            double_shift_type.setText(ShiftUtils.getChiType(double_shift_type_str));

            double_comment.setOnClickListener(this);
        }

        Button submit_btn = findViewById(R.id.order_confirm);
        submit_btn.setOnClickListener(this);
    }

    private void initRemindView() {
        TextView remind_bar = findViewById(R.id.order_setremind);
        remind_icon = findViewById(R.id.order_setremind_icon);
        TextView time_set = findViewById(R.id.order_setremindtime);
        TextView phone_location = findViewById(R.id.order_phonelocation);
        EditText phone_edit = findViewById(R.id.order_phoneedit);
        EditText mail_edit = findViewById(R.id.order_mailedit);

        remind_time = findViewById(R.id.order_remind_time);
        remind_phone = findViewById(R.id.order_remind_phone);
        remind_mail = findViewById(R.id.order_remind_mail);

        remind_bar.setOnClickListener(this);
        remind_icon.setOnClickListener(this);
        time_set.setOnClickListener(this);
        phone_location.setOnClickListener(this);
        phone_edit.setOnClickListener(this);
        mail_edit.setOnClickListener(this);
    }

    private void initNeedView(){
        TextView need_bar = findViewById(R.id.order_setneed);
        need_icon = findViewById(R.id.order_setneed_icon);
        CheckBox checkBox1 = findViewById(R.id.order_need_checkbox1);
        CheckBox checkBox2 = findViewById(R.id.order_need_checkbox2);
        CheckBox checkBox3 = findViewById(R.id.order_need_checkbox3);
        CheckBox checkBox4 = findViewById(R.id.order_need_checkbox4);
        need_front = findViewById(R.id.order_need1);
        need_back = findViewById(R.id.order_need2);
        need_window = findViewById(R.id.order_need3);
        need_other = findViewById(R.id.order_need4);

        need_bar.setOnClickListener(this);
        need_icon.setOnClickListener(this);

        setCheckChangeListener(checkBox1, 0);
        setCheckChangeListener(checkBox2, 1);
        setCheckChangeListener(checkBox3, 2);
        setCheckChangeListener(checkBox4, 3);
    }

    private void initExtendFlag(){
        remind_time.setVisibility(View.GONE);
        remind_phone.setVisibility(View.GONE);
        remind_mail.setVisibility(View.GONE);
        isRemindExtend = false;

        need_front.setVisibility(View.GONE);
        need_back.setVisibility(View.GONE);
        need_window.setVisibility(View.GONE);
        need_other.setVisibility(View.GONE);
        isNeedExtend = false;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.order_setremind:
            case R.id.order_setremind_icon:
                if (isRemindExtend){
                    setViewsGone(remind_time, remind_phone, remind_mail);
                    isRemindExtend = false;
                    remind_icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.more_48));
                }
                else{
                    setViewsVisible(remind_time, remind_phone, remind_mail);
                    isRemindExtend = true;
                    remind_icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.less_48));
                }
                break;
            case R.id.order_setneed:
            case R.id.order_setneed_icon:
                if (isNeedExtend){
                    setViewsGone(need_front, need_back, need_window, need_other);
                    isNeedExtend = false;
                    need_icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.more_48));
                }
                else{
                    setViewsVisible(need_front, need_back, need_window, need_other);
                    isNeedExtend = true;
                    need_icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.less_48));
                }
                break;
            case R.id.order_setremindtime:
                final TextView settime =  v.findViewById(v.getId());
                setAlertDialog("设置提醒时间", remind_list, settime, "");
                break;
            case R.id.order_phonelocation:
                final TextView location =  v.findViewById(v.getId());
                setAlertDialog("选择手机区域", phone_location_list, location, " :");
                break;
            case R.id.order_confirm:
                new AlertDialog.Builder(this)
                        .setMessage("确认提交预约申请吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestBody requestBody = new FormBody.Builder()
                                    .add("line_name", ShiftUtils.getLineByDepartureAndArrive(departure_place_str,arrive_place_str))
                                    .add("shift_id",shiftid_str)
                                    .add("appoint_date",date_str)
                                    .add("submit_time", StringCalendarUtils.getCurrentTime())
                                    .add("username",UserManager.getInstance().getUser().getUsername())
                                    .build();

                                submitAppoint(requestBody);
                                  //TODO:这段注释掉的要是没用就删了
//                                try {
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // order 到 单程
        if (isSingleWayFlag)
            finish();
        // order 到 第二页
        else{
            Intent appointDoubleIntent = new Intent(OrderActivity.this, AppointDoubleActivity.class);

            appointDoubleIntent.putExtra("single_departure_place", departure_place_str);
            appointDoubleIntent.putExtra("single_arrive_place", arrive_place_str);
            appointDoubleIntent.putExtra("single_departure_time", departure_time_str);
            //ToastUtils.showShort(StringCalendarUtils.HHmmssToHHmm(departure_time));
            appointDoubleIntent.putExtra("single_arrive_time", arrive_time_str);
            appointDoubleIntent.putExtra("single_departure_date", date_str);
            appointDoubleIntent.putExtra("single_shiftid", shiftid_str);
            appointDoubleIntent.putExtra("single_shift_type", shift_type_str);

            appointDoubleIntent.putExtra("double_departure_date", double_date_str);
            appointDoubleIntent.putExtra("target_page", 1);

//            startActivity(appointDoubleIntent);
            setResult(RESULT_OK, appointDoubleIntent);
            finish();
        }

       // finish();
    }

    private static String TAG = "orderactivity";

    public void submitAppoint(RequestBody requestBody){
        RetrofitClient.getBusApi()
            .appoint(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<HttpResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.i(TAG, "onsubscribe");
                    addDisposable(d);
                }

                @Override
                public void onNext(HttpResponse response) {
                    if(response.getError()==0){
                        //显示预约成功
                        String message = "";
                        if (isSingleWayFlag || (! isSingleWayFlag && !isOrderFinished)) {
                            message = "您已成功预约【" + date_str + " " + departure_time_str + "从" + departure_place_str
                                    + "开往" + arrive_place_str + "的" + shiftid_str + "号校区巴士】，请记得按时前去乘坐哦~";
                        } else if (isOrderFinished){
                            message = "您已成功预约【" + double_date_str + " " + double_departure_time_str + "从" + double_departure_place_str
                                    + "开往" + double_arrive_place_str + "的" + double_shiftid_str + "号校区巴士】，请记得按时前去乘坐哦~";
                        }

//                        Log.i(TAG, message);
                        new SweetAlertDialog(OrderActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("预约成功~")
                                .setContentText(message)
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        if (isSingleWayFlag) {
                                            OrderActivity.this.startActivity(new Intent(OrderActivity.this, MainActivity.class));
                                        } else if (! isOrderFinished){
                                            isOrderFinished = true;

                                            RequestBody anotherRequestBody = new FormBody.Builder()
                                                .add("line_name", ShiftUtils.getLineByDepartureAndArrive(double_departure_place_str,double_arrive_place_str))
                                                .add("shift_id",double_shiftid_str)
                                                .add("appoint_date",double_date_str)
                                                .add("submit_time", StringCalendarUtils.getCurrentTime())
                                                .add("username",UserManager.getInstance().getUser().getUsername())
                                                .build();
                                            submitAppoint(anotherRequestBody);

                                        } else {
                                            OrderActivity.this.startActivity(new Intent(OrderActivity.this, MainActivity.class));
                                        }
                                    }
                                })
                                .show();
                    }else{
//                        Log.i(TAG, "fail");
                        new SweetAlertDialog(OrderActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("预约失败!")
                                .setContentText("没有剩余座位或网络出错!")
                                .show();
                    }
                }
                @Override
                public void onError(Throwable e) {
                    Log.i(TAG, "onerror");
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.i(TAG, "onComplete ");
                    //mProgressBar.setVisibility(View.GONE);
                }
            });
    }

    private void setViewsVisible(LinearLayout... linearLayouts){
        for (LinearLayout layout : linearLayouts){
            layout.setVisibility(View.VISIBLE);
        }
    }
    private void setViewsGone(LinearLayout... linearLayouts){
        for (LinearLayout layout : linearLayouts){
            layout.setVisibility(View.GONE);
        }
    }

    private void setAlertDialog(String title, final String[] list, final TextView textView, final String more){
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        builder.setTitle(title);
        builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String text = list[which] + more;
                textView.setText(text);
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setCheckChangeListener(CheckBox checkBox, final int pos){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked_array[pos] = isChecked;
//                ToastUtils.showShort(check_msg[pos] + checked_array[pos]);
            }
        });
    }
}