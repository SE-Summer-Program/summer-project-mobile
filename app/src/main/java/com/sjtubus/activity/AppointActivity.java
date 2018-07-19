package com.sjtubus.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.model.AppointShortInfo;
import com.sjtubus.model.response.AppointResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.MyDateUtils;
import com.sjtubus.utils.ShiftUtils;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.AppointAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class AppointActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private AppointInfo appointInfo;
    private List<AppointInfo> appointInfoList;
    private AppointAdapter appointAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private String departure_place_str;
    private String arrive_place_str;
    private String date_str;
    private int year,month,day;
    private String line_name;
    private String line_type;

    private TextView left_appoint;
    private TextView yesterday_btn;
    private TextView nextday_btn;
    private TextView date;
    private ImageView calendar_btn;
    private ImageView next_btn;

    private boolean isTodayFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPassData();
        initToolbar();
        initView();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_appoint;
    }

    private void initPassData() {
        Intent intent = getIntent();
        departure_place_str = intent.getStringExtra("departure_place");
        arrive_place_str = intent.getStringExtra("arrive_place");
        date_str = intent.getStringExtra("singleway_date");
        line_name = ShiftUtils.getLineByDepartureAndArrive(departure_place_str, arrive_place_str);
        if(line_name.equals("error")) ToastUtils.showShort("地址翻译出现错误！");
    }

    private void initToolbar(){
        Toolbar mToolbar = findViewById(R.id.toolbar_appointment);
        mToolbar.setTitle(departure_place_str + "->" + arrive_place_str);
        mToolbar.setNavigationIcon(R.mipmap.icon_back_128);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data3 = (String) date.getText();
                Intent intent = new Intent(AppointActivity.this, AppointNaviActivity.class);
                intent.putExtra("departure_place", departure_place_str);
                intent.putExtra("arrive_place", arrive_place_str);
                intent.putExtra("singleway_date", data3);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initView() {
        yesterday_btn = findViewById(R.id.appoint_yesterday);
        nextday_btn = findViewById(R.id.appoint_nextday);
        date = findViewById(R.id.appoint_date);
        calendar_btn = findViewById(R.id.appoint_calendar);
        next_btn = findViewById(R.id.appoint_next);
        left_appoint = findViewById(R.id.appoint_left);

        yesterday_btn.setOnClickListener(this);
        nextday_btn.setOnClickListener(this);
        date.setOnClickListener(this);
        calendar_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);

        date.setText(date_str);

        if (StringCalendarUtils.isToday((String) date.getText())){
           // yesterday_btn.setEnabled(false);
            yesterday_btn.setTextColor(getResources().getColor(R.color.light_gray));
            isTodayFlag = true;
        }

        recyclerView = findViewById(R.id.appoint_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointAdapter = new AppointAdapter(this);
        recyclerView.setAdapter(appointAdapter);

        //滚动监听
        appointAdapter.setOnScrollListener(new AppointAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                recyclerView.scrollToPosition(pos);
            }
        });

        swipeRefresh = findViewById(R.id.refresh_appoint);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveData();
            }
        });

        getCurrentDay();
        retrieveData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appoint_calendar:
                break;
            case R.id.appoint_next:
                break;
            case R.id.appoint_date:
                yesterday_btn.setEnabled(true);
                yesterday_btn.setTextColor(getResources().getColor(R.color.primary_white));
                isTodayFlag = false;

                final TextView textView_date = v.findViewById(R.id.appoint_date);

              //  Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(AppointActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year_choose, int month_choose, int dayOfMonth_choose) {

                        String datestr = year_choose+"-"+(month_choose+1)+"-"+dayOfMonth_choose;

                        if (StringCalendarUtils.isBeforeCurrentDate(datestr)){
                            ToastUtils.showShort("不能预约已经发出的班次哦~");
                            return;
                        }
                        //textView_date.setText(year_choose+"-"+(month_choose+1)+"-"+dayOfMonth_choose);
                        String monthStr = StringCalendarUtils.getDoubleDigitMonth(month_choose);
                        String dayStr = StringCalendarUtils.getDoubleDigitDay(dayOfMonth_choose);
                        String dateStr = year_choose+"-"+monthStr+"-"+dayStr; //格式2018-07-19
                        textView_date.setText(dateStr);
                        /*
                         * 统一日期格式为 yyyy-MM-dd
                         */
                        year = year_choose;
                        month = month_choose+1;
                        day = dayOfMonth_choose;
                    }
                }, year,month,day).show();
                retrieveData();

                //如果当前日期是今天，则前一天不可用
                if (StringCalendarUtils.isToday((String) date.getText())){
                   // yesterday_btn.setEnabled(false);
                    yesterday_btn.setTextColor(getResources().getColor(R.color.light_gray));
                    isTodayFlag = true;
                }

                break;
            case R.id.appoint_yesterday:
                if (isTodayFlag){
                    yesterday_btn.setEnabled(false);
                    ToastUtils.showShort("不能预约更前面的班次了哦~");
                    break;
                }
//                modifyDate(-1);
                ToastUtils.showShort("前一天");
                String yesterday = MyDateUtils.getYesterdayStr((String) date.getText());
                date.setText(yesterday);
                if (StringCalendarUtils.isToday((String) date.getText())){
                    //yesterday_btn.setEnabled(false);
                    yesterday_btn.setTextColor(getResources().getColor(R.color.light_gray));
                    isTodayFlag = true;
                }
                retrieveData();
                break;
            case R.id.appoint_nextday:
                ToastUtils.showShort("后一天");
//                modifyDate(1);
                String tomorrow = MyDateUtils.getTomorrowStr((String) date.getText());
                date.setText(tomorrow);
                yesterday_btn.setEnabled(true);
                yesterday_btn.setTextColor(getResources().getColor(R.color.primary_white));
                isTodayFlag = false;
                retrieveData();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        String data1 = departure_place_str;
        String data2 = arrive_place_str;
        String data3 = (String) date.getText();
        Intent intent = new Intent(AppointActivity.this, AppointNaviActivity.class);
        intent.putExtra("departure_place", data1);
        intent.putExtra("arrive_place", data2);
        intent.putExtra("singleway_date", data3);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void retrieveData() {
        Log.d("retrivedata", "start");

        Calendar calendar = StringCalendarUtils.StringToCalendar((String) date.getText());
        line_type = ShiftUtils.getTypeByCalendar(calendar);
        String appoint_date = (String) date.getText();

        RetrofitClient.getBusApi()
                .getAppointment(line_name, line_type, appoint_date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AppointResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(AppointResponse response) {
                    Log.d(TAG, "onNext: ");
                    List<AppointInfo> infos = new ArrayList<>();
                    int i = 0;
                    for(AppointShortInfo shortinfo:response.getAppointment()){
                        AppointInfo info = new AppointInfo();
                        info.setShiftid(shortinfo.getShiftid());
                        info.setArrive_time(shortinfo.getArrive_time());
                        info.setDeparture_time(shortinfo.getDeparture_time());
                        info.setRemain_seat(shortinfo.getRemain_seat());
                        info.setId(i+"");
                        info.setLine_type(line_type);
                        info.setType(0);
                        info.setDate(date_str);
                        info.setArrive_place(arrive_place_str);
                        info.setDeparture_place(departure_place_str);
                        info.setAppoint_status(shortinfo.getRemain_seat()>0?1:0) ;
                        i++;
                        infos.add(info);
                    }
                    String left_appoint_info = "当日剩余可预约班次: "+infos.size();
                    left_appoint.setText(left_appoint_info);
                    appointAdapter.setDataList(infos);
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    ToastUtils.showShort("网络请求失败！请检查你的网络！");
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: ");
                    //mProgressBar.setVisibility(View.GONE);
                }
            });
    }

    private void getCurrentDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(MyDateUtils.getBeginOfDay((String) date.getText()));
        year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        month = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

//    private void modifyDate(int offset){
//        switch(offset){
//            case 1:
//                //...后一天
//                break;
//            case -1:
//                //...前一天
//                break;
//        }
//    }
}
