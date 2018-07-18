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
import com.sjtubus.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class OrderActivity extends BaseActivity implements View.OnClickListener{

    private TextView departure_place, departure_time, departure_date;
    private TextView arrive_place, arrive_time, arrive_date;

    private TextView shiftid;
    private TextView shift_type;
    private TextView comment;

    private String departure_place_str, arrive_place_str;
    private String departure_time_str, arrive_time_str;
    private String date_str;
    private String shiftid_str;
    private String shift_type_str;

    private TextView remind_bar, need_bar;
    private ImageView remind_icon, need_icon;
    private LinearLayout remind_time, remind_phone, remind_mail;
    private LinearLayout need_front, need_back, need_window, need_other;

    private TextView time_set, phone_location;
    private EditText phone_edit, mail_edit;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    private EditText comment1, comment2, comment3, comment4;
    private ImageView comment1_clear, comment2_clear, comment3_clear, comment4_clear;

    private Button submit_btn;

    private boolean isRemindExtend, isNeedExtend;
    private String[] remind_list = {"提前10分钟","提前30分钟","提前1小时","提前2小时","不提醒"};
    private String[] phone_location_list ={"大陆地区", "港澳台地区", "国外地区"};
    private boolean[] checked_array = {false, false, false, false};
    private String[] check_msg = {"前排座位", "后排座位", "靠窗座位", "其他特殊要求"};

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
        Intent intent = getIntent();
        departure_place_str = intent.getStringExtra("departure_place");
        arrive_place_str = intent.getStringExtra("arrive_place");
        departure_time_str = intent.getStringExtra("departure_time");
        arrive_time_str = intent.getStringExtra("arrive_time");
        date_str = intent.getStringExtra("departure_date");
        shiftid_str = intent.getStringExtra("shiftid");
        shift_type_str = intent.getStringExtra("shift_type");
    }

    private void initToolbar(){
        Toolbar mToolbar = findViewById(R.id.toolbar_order);
        mToolbar.setNavigationIcon(R.drawable.back_32);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        departure_place = findViewById(R.id.order_departureplace);
        departure_time = findViewById(R.id.order_departuretime);
        departure_date = findViewById(R.id.order_departuredate);
        arrive_place = findViewById(R.id.order_arriveplace);
        arrive_time = findViewById(R.id.order_arrivetime);
        arrive_date = findViewById(R.id.order_arrivedate);
        shiftid = findViewById(R.id.order_shiftid);
        shift_type = findViewById(R.id.order_shifttype);
        comment = findViewById(R.id.order_comment);

        departure_place.setText(departure_place_str);
        arrive_place.setText(arrive_place_str);
        departure_time.setText(departure_time_str);
        arrive_time.setText(arrive_time_str);
        departure_date.setText(date_str);
        arrive_date.setText(date_str);
        shiftid.setText(shiftid_str);
        shift_type.setText(shift_type_str);

        comment.setOnClickListener(this);

        submit_btn = findViewById(R.id.order_confirm);
        submit_btn.setOnClickListener(this);
    }

    private void initRemindView() {
        remind_bar = findViewById(R.id.order_setremind);
        remind_icon = findViewById(R.id.order_setremind_icon);
        time_set = findViewById(R.id.order_setremindtime);
        phone_location = findViewById(R.id.order_phonelocation);
        phone_edit = findViewById(R.id.order_phoneedit);
        mail_edit = findViewById(R.id.order_mailedit);

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
        need_bar = findViewById(R.id.order_setneed);
        need_icon = findViewById(R.id.order_setneed_icon);
        checkBox1 = findViewById(R.id.order_need_checkbox1);
        checkBox2 = findViewById(R.id.order_need_checkbox2);
        checkBox3 = findViewById(R.id.order_need_checkbox3);
        checkBox4 = findViewById(R.id.order_need_checkbox4);
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
                if (isRemindExtend == true){
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
                if (isNeedExtend == true){
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
                final TextView settime = (TextView) v.findViewById(v.getId());
                setAlertDialog("提醒", remind_list, settime, "");
                break;
            case R.id.order_phonelocation:
                final TextView location = (TextView) v.findViewById(v.getId());
                setAlertDialog("手机区域", phone_location_list, location, " :");
                break;
            case R.id.order_confirm:
                submitAppoint();
                break;
        }
    }

    public void submitAppoint(){
        RequestBody requestBody = new FormBody.Builder()
                .add("line_name", ShiftUtils.getLineByDepartureAndArrive(departure_place_str,arrive_place_str))
                .add("shift_id",shiftid_str)
                .add("appoint_date",date_str)
                .add("username",UserManager.getInstance().getUser().getUsername())
                .build();
        RetrofitClient.getBusApi()
                .appoint(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(HttpResponse response) {
                        if(response.getError()==0){
                            //显示预约成功
                            String message = "您已成功预约" + date_str + departure_time_str + "从" + departure_place_str
                                    + "开往" + arrive_place_str + "的" + shiftid_str +"号校区巴士，请记得按时前去乘坐哦~";
                            new AlertDialog.Builder(OrderActivity.this)
                                    .setTitle("预约成功！")
                                    .setMessage(message)
                                    .setPositiveButton("确定", null)
                                    .show();
                            OrderActivity.this.startActivity(new Intent(OrderActivity.this,MainActivity.class));
                        }else ToastUtils.showShort(response.getMsg());
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
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
                textView.setText(list[which] + more);
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
                ToastUtils.showShort(check_msg[pos] + checked_array[pos]);
            }
        });
    }
}