package com.sjtubus.activity;

import android.os.Bundle;
import android.view.View;

import com.sjtubus.R;

public class RecordActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order;
    }

    @Override
    public void onClick(View v){

    }
}
