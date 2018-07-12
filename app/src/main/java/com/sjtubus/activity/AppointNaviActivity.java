package com.sjtubus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.sjtubus.R;
import com.sjtubus.fragment.AppointFragment;
import com.sjtubus.utils.GlideImageLoader;
import com.sjtubus.widget.AppointPagerAdapter;
import com.sjtubus.widget.SchedulePagerAdapter;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class AppointNaviActivity extends BaseActivity implements View.OnClickListener{

    CoordinatorTabLayout mCoordinatorTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragment;
    private final String[] mTitles = {"单程", "往返"};

    private Toolbar toolbar;

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

//        toolbar = findViewById(R.id.toolbar_appointnavi);
//        toolbar.setTitle("");

     //   initBanner();

    }

//    private void initBanner(){
//        Banner banner = findViewById(R.id.banner);
//        loadImages();
//        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();
//    }
//
//    private void loadImages(){
//        images.add("http://chuantu.biz/t6/337/1530513364x-1566688664.jpg");
//        images.add("http://chuantu.biz/t6/337/1530513397x-1566688664.jpg");
//        images.add("http://chuantu.biz/t6/337/1530513420x-1566688664.jpg");
//    }

    private void initFragments(){
        mFragment = new ArrayList<>();
        for (String title : mTitles) {
            mFragment.add(AppointFragment.getInstance());
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
