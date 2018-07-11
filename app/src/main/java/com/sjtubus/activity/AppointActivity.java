package com.sjtubus.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sjtubus.R;
import com.sjtubus.model.AppointDataBean;
import com.sjtubus.widget.AppointAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppointActivity extends BaseActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AppointDataBean appointDataBean;
    private List<AppointDataBean> appointDataBeanList;
    private AppointAdapter appointAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = findViewById(R.id.toolbar_appointment);
        toolbar.setTitle("");
        Log.d("appointment", "alive 1");

        recyclerView = (RecyclerView)findViewById(R.id.appoint_recycle);
        initData();
    }

    @Override
    public int getContentViewId(){
        return R.layout.activity_appoint;
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
