package com.sjtubus.activity;

import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
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
  //  Button calendar_btn;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ScheduleAdapter adapter;

    Calendar calendar;
  //  Date date;

    private List<Schedule> schedules = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_schedule);

        initViews();

        recyclerView = (RecyclerView)findViewById(R.id.recycle_schedule);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); //设置布局管理器
        layoutManager.setOrientation(OrientationHelper.VERTICAL); //设置为垂直布局，默认

        String type = getTypes();
        setAndShowSchedule(type);
    }

    public void initViews(){
        mToolbar = findViewById(R.id.toolbar_schedule);
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.primary_red,null));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.primary_white,null));
        mToolbar.setNavigationIcon(R.mipmap.calendar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDlg();
            }
        });
       // mToolbar.inflateMenu(R.menu.schedule_types);
    }

    public void setAndShowSchedule(String type){
        Schedule MinToXu = new Schedule("MinToXu", type);
        schedules.add(MinToXu);
        Schedule XuToMin = new Schedule("XuToMin", type);
        schedules.add(XuToMin);
        Schedule MinToQi = new Schedule("MinToQi", type);
        schedules.add(MinToQi);
        Schedule QiToMin = new Schedule("QiToMin", type);
        schedules.add(QiToMin);

        adapter = new ScheduleAdapter(schedules);
        recyclerView.setAdapter(adapter); //设置adapter
        // recyclerView.setItemAnimator(new DefaultItemAnimator()); //设置条目增删动画
    }

    public String getTypes(){
        calendar = Calendar.getInstance();
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
            return "HoildayWorkday";
        }
        else{
            return "HoildayWeekend";
        }
    }

    public boolean isWeekend(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY)
            return true;
        else
            return false;
    }
    public boolean isHoilday(Calendar calendar){
        int month = calendar.get(Calendar.MONTH);
        if (month == Calendar.FEBRUARY || month == Calendar.JUNE || month == Calendar.JULY)
            return true;
        else
            return false;
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

        //测试canlendar
        //Toast.makeText(this, calendar.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_types, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String type = "NormalWorkday";
        switch (item.getItemId()){
            case R.id.schedule_normalWorkday:
                break;
            case R.id.schedule_normalWeekendAndLegalHoilday:
                type = "NormalWeekendAndLegalHoilday";
                break;
            case R.id.schedule_hoildayWorkday:
                type = "HoildayWorkday";
                break;
            case R.id.schedule_hoildayWeekend:
                type = "HoildayWeekend";
                break;
        }
        setAndShowSchedule(type);
        return true;
    }
}
