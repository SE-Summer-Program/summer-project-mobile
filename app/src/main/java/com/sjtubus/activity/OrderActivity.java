package com.sjtubus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjtubus.R;

public class OrderActivity extends BaseActivity implements View.OnClickListener{

    private Toolbar mToolbar;

    private TextView departure_place;
    private TextView departure_time;
    private TextView shiftid;
    private TextView comment;
    private TextView departure_date;
    private TextView arrive_place;
    private TextView arrive_time;
    private TextView yesterday_btn;
    private TextView nextday_btn;
    private TextView date;
    private ImageView calendar_btn;
    private ImageView next_btn;

    private String departure_place_str;
    private String arrive_place_str;
    private String departure_date_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPassData();
        initToolbar();
        initView();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order;
    }

    private void initPassData(){
        Intent intent = getIntent();
        departure_place_str = intent.getStringExtra("departure_place");
        arrive_place_str = intent.getStringExtra("arrive_place");
        departure_date_str = intent.getStringExtra("departure_date");
    }

    private void initToolbar(){
        mToolbar = findViewById(R.id.toolbar_order);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.back_32);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appointIntent = new Intent(OrderActivity.this, AppointActivity.class);
                startActivity(appointIntent);
            }
        });
    }

    private void initView() {
        departure_place = findViewById(R.id.order_departureplace);
        departure_time = findViewById(R.id.order_departuretime);
        shiftid = findViewById(R.id.order_shiftid);
        comment = findViewById(R.id.order_comment);
        departure_date = findViewById(R.id.order_departuredate);
        arrive_place = findViewById(R.id.order_arriveplace);
        arrive_time = findViewById(R.id.order_arrivetime);

        departure_place.setText(departure_place_str);
        arrive_place.setText(arrive_place_str);
        departure_date.setText(departure_date_str);
    }

    @Override
    public void onClick(View v){

    }
}
