package com.sjtubus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sjtubus.R;
import com.sjtubus.fragment.ShiftFragment;
import com.sjtubus.fragment.StationFragment;
import com.sjtubus.widget.SchedulePagerAdapter;

import java.util.ArrayList;

import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class ScheduleActivity extends BaseActivity {
    private String line_name;
    private String line_type;

    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private final String[] mTitles = {"时刻表", "路线站点"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        line_name = intent.getStringExtra("LINE_NAME");
        line_type = intent.getStringExtra("LINE_TYPE");
        initView();
    }

    public void initView(){
        initFragments();
        initViewPager();
        CoordinatorTabLayout mCoordinatorTabLayout = findViewById(R.id.coordinatortablayout);
        int[] mImageArray = new int[]{
                R.drawable.bus_background,
                R.drawable.bus_background};
        int[] mColorArray = new int[]{
                R.color.primary_red,
                R.color.primary_red};
        mCoordinatorTabLayout.setTranslucentStatusBar(this);
        mCoordinatorTabLayout.setTitle(line_name+"("+line_type+")")
        .setImageArray(mImageArray, mColorArray)
        .setupWithViewPager(mViewPager);
    }

    public void initFragments(){
        mFragments = new ArrayList<>();
        mFragments.add(ShiftFragment.getInstance(line_type,line_name));
        mFragments.add(StationFragment.getInstance(line_name));
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.schedule_vp);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new SchedulePagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }

    @Override
    public int getContentViewId(){
        return R.layout.activity_schedule;
    }
}
