package com.sjtubus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.sjtubus.R;

import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.widget.LineAdapter;
import com.sjtubus.model.response.LineInfoResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class LineActivity extends BaseActivity implements LineAdapter.OnItemClickListener{

    Toolbar mToolbar;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    LineAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    Calendar calendar;
    MyDialogListener dialogListener = new MyDialogListener();
    private int select = 0;

    private String[] type_list = {"在校期-工作日", "在校期-双休日、节假日", "寒暑假-工作日","寒暑假-双休日"};
    private String[] type_list_E = {"NormalWorkday","NormalWeekendAndLegalHoliday","HolidayWorkday","HolidayWeekend"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    public int getContentViewId(){
        return R.layout.activity_line;
    }

    public void initViews(){
        mToolbar = findViewById(R.id.toolbar_line);
        //setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDialog();
            }
        });

        recyclerView = findViewById(R.id.recycle_schedule);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); //设置布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); //设置为垂直布局，默认
       // recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //分割线
        adapter = new LineAdapter(this);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        String type = "NormalWorkday";
        setAndShowSchedule(type);

        swipeRefresh = findViewById(R.id.refresh_schedule);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshSchedule();
            }
        });
    }

    public void setAndShowSchedule(String type){
        //Log.d("LineActivity", type);
        RetrofitClient.getBusApi()
            .getLineInfos(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<LineInfoResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(LineInfoResponse response) {
                    Log.d(TAG, "onNext: ");
                    adapter.setDataList(response.getLineInfos());
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


    @Override
    public void onItemClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        String line_name = adapter.getLinename(position);
        Intent schedule_intent = new Intent(LineActivity.this, ScheduleActivity.class);
        schedule_intent.putExtra("LINE_TYPE",type_list_E[select]);
        schedule_intent.putExtra("LINE_NAME",line_name);
        startActivity(schedule_intent);
    }

    public void putDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder((LineActivity.this));
        builder.setTitle("选择时间段");
        builder.setIcon(R.mipmap.type);
        builder.setSingleChoiceItems(type_list, select, dialogListener);
        builder.setCancelable(false);
        builder.setPositiveButton("确定",dialogListener);
        builder.setNegativeButton("取消",dialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void refreshSchedule(){
        RetrofitClient.getBusApi()
            .getLineInfos(type_list_E[select])
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<LineInfoResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(LineInfoResponse response) {
                    Log.d(TAG, "onNext: ");
                    adapter.setDataList(response.getLineInfos());
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    swipeRefresh.setRefreshing(false);
                    ToastUtils.showShort("网络请求失败！请检查你的网络！");
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: ");
                    //mProgressBar.setVisibility(View.GONE);
                }
            });
    }


    private class MyDialogListener implements DialogInterface.OnClickListener{
        private int temp_select = 0;
        @Override
        public void onClick(DialogInterface dialog, int which){
            if(which == BUTTON_POSITIVE){
                select = temp_select;
                setAndShowSchedule(type_list_E[select]);
            }else if(which == BUTTON_NEGATIVE){
                temp_select = 0;
            }else{
                temp_select = which;
            }
        }
    }

    public String getTypes(){
        calendar = Calendar.getInstance();
        //date = calendar.getTime();
        boolean isWeekendFlag = StringCalendarUtils.isWeekend(calendar);
        boolean isHoildayFlag = StringCalendarUtils.isHoilday(calendar);
        if (!isHoildayFlag && !isWeekendFlag){
            return "NormalWorkday";
        }
        else if (!isHoildayFlag){
            return "NormalWeekendAndLegalHoilday";
        }
        else if (!isWeekendFlag){
            return "HoildayWorkday";
        }
        else{
            return "HoildayWeekend";
        }
    }

    public void showDatePickDlg(){
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(LineActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //date = calendar.getTime();
                //这个时候calendar有没有被改变??
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
}
