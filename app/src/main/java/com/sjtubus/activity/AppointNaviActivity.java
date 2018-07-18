package com.sjtubus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.sjtubus.R;
import com.sjtubus.fragment.AppointNaviFragment;
import com.sjtubus.widget.AppointPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class AppointNaviActivity extends BaseActivity implements View.OnClickListener{

    CoordinatorTabLayout mCoordinatorTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragment;
    private final String[] mTitles = {"单程", "往返"};

    private List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_appointnavi;
    }

    private void initView(){
        initFragments();
        initViewPager();

        mCoordinatorTabLayout = findViewById(R.id.appoint_coordinatortablayout);
        int[] mImageArray = new int[]{
                R.drawable.bus_background,
                R.drawable.bus_background};
        int[] mColorArray = new int[]{
                R.color.primary_red,
                R.color.primary_red};

        mCoordinatorTabLayout.setTranslucentStatusBar(this)
                .setTitle("预约班车")
                .setBackEnable(true)
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager);
    }

    private void initFragments(){
        mFragment = new ArrayList<>();
        for (String title : mTitles) {
            mFragment.add(AppointNaviFragment.getInstance(title));
        }
    }

    private void initViewPager(){
        mViewPager = (ViewPager)findViewById(R.id.appoint_vp);
        AppointPagerAdapter adapter = new AppointPagerAdapter(getSupportFragmentManager(),mFragment,mTitles);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
