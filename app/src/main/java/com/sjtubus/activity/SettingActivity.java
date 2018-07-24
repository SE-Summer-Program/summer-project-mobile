package com.sjtubus.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;

import java.util.Arrays;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }


    private void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        ToastUtils.showShort("设置功能还不能使用哦~");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}

