package com.sjtubus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Short4;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.sjtubus.R;

import com.sjtubus.model.Schedule;
import com.sjtubus.model.ScheduleAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends BaseActivity implements View.OnClickListener{

    Toolbar mToolbar;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ScheduleAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    Calendar calendar;

    private List<Schedule> schedules = new ArrayList<>();
    private String[] type_list = {"在校期-工作日", "在校期-双休日、节假日", "寒暑假-工作日","寒暑假-双休日"};
    private String[] type_list_E = {"NormalWorkday","NormalWeekendAndLegalHoilday","HoildayWorkday","HoildayWeekend"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    public int getContentViewId(){
        return R.layout.activity_schedule;
    }

    public void initViews(){
        mToolbar = findViewById(R.id.toolbar_schedule);
      //  setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.primary_red,null));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.primary_white,null));
//        mToolbar.setNavigationIcon(R.mipmap.calendar);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickDlg();
//            }
//        });
        mToolbar.setNavigationIcon(R.drawable.menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDialog();
            }
        });

       // mToolbar.inflateMenu(R.menu.schedule_types);

        recyclerView = (RecyclerView)findViewById(R.id.recycle_schedule);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); //设置布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); //设置为垂直布局，默认
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //分割线

        String type = getTypes();
        setAndShowSchedule(type);

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.refresh_schedule);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshSchedule();
            }
        });
    }

    public void setAndShowSchedule(String type){
        Toast.makeText(this, type, Toast.LENGTH_LONG).show();
      //  Log.d("ScheduleActivity", type);
        Schedule MinToXu = new Schedule("闵行->徐汇", type);
        schedules.add(MinToXu);
        Schedule XuToMin = new Schedule("徐汇->闵行", type);
        schedules.add(XuToMin);
        Schedule MinToQi = new Schedule("闵行->七宝", type);
        schedules.add(MinToQi);
        Schedule QiToMin = new Schedule("七宝->闵行", type);
        schedules.add(QiToMin);

        adapter = new ScheduleAdapter(this, schedules);
        recyclerView.setAdapter(adapter); //设置adapter
        // recyclerView.setItemAnimator(new DefaultItemAnimator()); //设置条目增删动画
    }

    public String getTypes(){
        calendar = Calendar.getInstance();
        Toast.makeText(this, calendar.toString(), Toast.LENGTH_SHORT).show();
     //   date = calendar.getTime();
        boolean isWeekendFlag = isWeekend(calendar);
        boolean isHoildayFlag = isHoilday(calendar);
        if (!isHoildayFlag && !isWeekendFlag){
            return "NormalWorkday";
        }
        else if (!isHoildayFlag && isWeekendFlag){
            return "NormalWeekendAndLegalHoilday";
        }
        else if (isHoildayFlag && !isWeekendFlag){
        //    Toast.makeText(this, "return hoildayworkday", Toast.LENGTH_SHORT).show();
            return "HoildayWorkday";
        }
        else{
            return "HoildayWeekend";
        }
    }
    public boolean isWeekend(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) return true;
        else return false;
    }
    public boolean isHoilday(Calendar calendar){
        int month = calendar.get(Calendar.MONTH);
        if (month == Calendar.FEBRUARY || month == Calendar.JUNE || month == Calendar.JULY) return true;
        else return false;
    }

    public void showDatePickDlg(){
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //date = calendar.getTime();
                //这个时候calendar有没有被改变??
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){

        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.schedule_menu, menu);
//        return true;
//    }
//
//    @Override
    public void putDialog() {
//        switch (item.getItemId()){
//
//            case R.id.setting:
                AlertDialog.Builder builder = new AlertDialog.Builder((ScheduleActivity.this));
                builder.setTitle("选择时间段");
                builder.setIcon(R.drawable.type);
                builder.setSingleChoiceItems(type_list, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String type = type_list[which];
                        Toast.makeText(ScheduleActivity.this, type, Toast.LENGTH_SHORT).show();
                        setAndShowSchedule(type_list_E[which]);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
//                break;
//        }
//        return true;
    }

    public void refreshSchedule(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // initViews();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
