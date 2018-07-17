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
import com.sjtubus.model.Appointment;
import com.sjtubus.model.response.AppointResponse;
import com.sjtubus.model.response.ScheduleResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.ShiftUtils;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.AppointAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class AppointActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private Appointment appointment;
    private List<Appointment> appointmentList;
    private AppointAdapter appointAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private String[] station_list = {"闵行校区", "徐汇校区", "七宝校区"};
    private String[] line_list = {"闵行到徐汇", "徐汇到闵行", "闵行到七宝", "七宝到闵行"};
    private String[] line_list_E = {"MinToXu", "XuToMin", "MinToQi", "QiToMin"};

    private String departure_place_str;
    private String arrive_place_str;
    private String date_str;
    private int year,month,day;
    private String line_name;
    private String line_type;

    private TextView yesterday_btn;
    private TextView nextday_btn;
    private TextView date;
    private ImageView calendar_btn;
    private ImageView next_btn;

    private AppointAdapter mAdapter;

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
    }

    private void initToolbar(){
        mToolbar = findViewById(R.id.toolbar_appointment);
        mToolbar.setTitle(departure_place_str + "->" + arrive_place_str);
        mToolbar.setNavigationIcon(R.mipmap.icon_back_128);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String data1 = (String) parseDeparturePlace(mToolbar);
//                String data2 = (String) parseArrivePlace(mToolbar);
                String data1 = "闵行校区";
                String data2 = "徐汇校区";
                String data3 = (String) date.getText();
                Intent intent = new Intent(AppointActivity.this, AppointNaviActivity.class);
                intent.putExtra("departure_place", data1);
                intent.putExtra("arrive_place", data2);
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

        yesterday_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        date.setOnClickListener(this);
        calendar_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);

        date.setText(date_str);

        recyclerView = (RecyclerView) findViewById(R.id.appoint_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // appointAdapter = new AppointAdapter(this, appointmentList);
        appointAdapter = new AppointAdapter(this);
        recyclerView.setAdapter(appointAdapter);

        //滚动监听
        appointAdapter.setOnScrollListener(new AppointAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                recyclerView.scrollToPosition(pos);
            }
        });

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh_appoint);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAppoint();
            }
        });

        retrieveData();
        //initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appoint_calendar:
            case R.id.appoint_next:
            case R.id.appoint_date:
                final TextView textView_date = (TextView) v.findViewById(R.id.appoint_date);

              //  Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(AppointActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year_choose, int month_choose, int dayOfMonth_choose) {
                        textView_date.setText(year_choose+"年"+(month_choose+1)+"月"+dayOfMonth_choose+"日");
                        year = year_choose;
                        month = month_choose+1;
                        day = dayOfMonth_choose;
                    }
                }, year,month,day).show();
                break;
            case R.id.appoint_yesterday:
                ToastUtils.showShort("前一天");
                //modifyDate(-1);

                //临时写在这里的
                String data1 = departure_place_str;
                String data2 = arrive_place_str;
                String data3 = "12:15";
                String data4 = "13:15";
                String data5 = (String) date.getText();
                String data6 = "MXHD1215";
                String data7 = "寒暑假工作日";
                Intent orderIntent = new Intent(AppointActivity.this, OrderActivity.class);
                orderIntent.putExtra("departure_place", data1);
                orderIntent.putExtra("arrive_place", data2);
                orderIntent.putExtra("departure_time", data3);
                orderIntent.putExtra("arrive_time", data4);
                orderIntent.putExtra("departure_date", data5);
                orderIntent.putExtra("shiftid", data6);
                orderIntent.putExtra("shift_type", data7);
                startActivity(orderIntent);
                break;

            case R.id.appoint_nextday:
                ToastUtils.showShort("后一天");
                modifyDate(1);
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

    private void refreshAppoint(){ retrieveData();}

    private void retrieveData() {
        Calendar calendar = StringCalendarUtils.StringToCalendar((String) date.getText());
        line_type = ShiftUtils.getTypeByCalendar(calendar);
        RetrofitClient.getBusApi()
            .getAppointment(line_name, line_type)
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
                    mAdapter.setDataList(response.getAppointment());
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

//    private void initData() {
//        appointmentList = new ArrayList<>();
//        for (int i = 1; i <= 20; i++) {
//            appointment = new Appointment();
//            appointment.setId(i + "");
//            appointment.setType(0);
//            appointment.setShiftid("MXWD0745");
//            appointment.setDeparture_place("始：闵行");
//            appointment.setArrive_place("终：徐汇");
//            appointment.setDeparture_time("07:45");
//            appointment.setArrive_time("08:45");
//            appointment.setRemain_seat(5);
//            appointmentList.add(appointment);
//        }
//        setData();
//    }

    private void setData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // appointAdapter = new AppointAdapter(this, appointmentList);

        appointAdapter = new AppointAdapter(this);
        recyclerView.setAdapter(appointAdapter);

        //滚动监听
        appointAdapter.setOnScrollListener(new AppointAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                recyclerView.scrollToPosition(pos);
            }
        });
    }

    private void getConrrentDay() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);       //获取年月日时分秒
        month = calendar.get(Calendar.MONTH)+1;   //获取到的月份是从0开始计数
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void modifyDate(int offset){
        switch(offset){
            case 1:
                //...后一天
                break;
            case -1:
                //...前一天
                break;
        }
    }
}
