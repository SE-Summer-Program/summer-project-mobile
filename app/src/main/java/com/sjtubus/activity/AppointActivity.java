package com.sjtubus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sjtubus.R;
import com.sjtubus.model.AppointDataBean;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.AppointAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointActivity extends BaseActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private AppointDataBean appointDataBean;
    private List<AppointDataBean> appointDataBeanList;
    private AppointAdapter appointAdapter;
    private ToastUtils toastUtils;

    private String[] station_list = {"闵行校区", "徐汇校区", "七宝校区" };
    private String departure_place;
    private String arrive_place;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPassData();
        initView();
    }

    @Override
    public int getContentViewId(){
        return R.layout.activity_appoint;
    }

    private void initPassData(){
        Intent intent = getIntent();
        departure_place = intent.getStringExtra("departure_place");
        arrive_place = intent.getStringExtra("arrive_place");
        date = intent.getStringExtra("singleway_date");
        //toastUtils.showShort(departure_place);
    }

    private void initView(){
        mToolbar = findViewById(R.id.toolbar_appointment);
        mToolbar.setTitle("");

        recyclerView = (RecyclerView)findViewById(R.id.appoint_recycle);
        initData();
    }

    private void initData(){
        appointDataBeanList = new ArrayList<>();
        for (int i = 1; i <= 20; i++){
            appointDataBean = new AppointDataBean();
            appointDataBean.setId(i+"");
            appointDataBean.setType(0);
            appointDataBean.setShiftid("MXWD0745");
            appointDataBean.setDeparture_place("始：闵行");
            appointDataBean.setArrive_place("终：徐汇");
            appointDataBean.setDeparture_time("07:45");
            appointDataBean.setArrive_time("08:45");
            appointDataBean.setRemain_seat(5);
            appointDataBean.setChild_msg("哈哈哈");
            appointDataBeanList.add(appointDataBean);
            Log.d("appointment", "alive 2");
        }
        setData();
    }

    private void setData(){
        Log.d("appointment", "alive 3");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointAdapter = new AppointAdapter(this, appointDataBeanList);
        recyclerView.setAdapter(appointAdapter);

        //滚动监听
        appointAdapter.setOnScrollListener(new AppointAdapter.OnScrollListener(){
            @Override
            public void scrollTo(int pos) {
                recyclerView.scrollToPosition(pos);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
