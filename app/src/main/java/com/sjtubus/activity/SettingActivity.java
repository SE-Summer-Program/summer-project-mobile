package com.sjtubus.activity;

import android.os.Bundle;
import android.view.View;

import com.sjtubus.R;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    private void initView(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
