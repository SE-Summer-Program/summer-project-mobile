package com.sjtubus.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.feedback.FeedbackAgent;
import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;
import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.utils.GlideImageLoader;
import com.sjtubus.utils.ToastUtils;
import com.youth.banner.Banner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{

    Toolbar mToolbar;
    Button reserve_btn;
    Button record_btn;
    Button position_btn;
    Button schedule_btn;
    Button map_btn;
    Button navigate_btn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ToastUtils toastUtils;

    TextView tab_home;
    TextView tab_message;
    TextView tab_user;
    TextView tab_setting;

    View decorView;

    private List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        Banner banner = findViewById(R.id.banner);
        initView();
        loadImages();
        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();

        //获取顶层视图
        decorView = getWindow().getDecorView();

//        decorView = getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected void onStart() {
        init();
        super.onStart();
    }

    public int getContentViewId(){
        return R.layout.activity_main;
    }

    public void initView(){
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.person);

        reserve_btn = findViewById(R.id.reserve_btn);
        record_btn = findViewById(R.id.record_btn);
        position_btn = findViewById(R.id.position_btn);
        schedule_btn = findViewById(R.id.schedule_btn);
        map_btn = findViewById(R.id.map_btn);
        navigate_btn = findViewById(R.id.navigate_btn);
        tab_home = findViewById(R.id.tabmenu_home);
        tab_message = findViewById(R.id.tabmenu_message);
        tab_user = findViewById(R.id.tabmenu_user);
        tab_setting = findViewById(R.id.tabmenu_setting);

        reserve_btn.setOnClickListener(this);
        record_btn.setOnClickListener(this);
        position_btn.setOnClickListener(this);
        schedule_btn.setOnClickListener(this);
        map_btn.setOnClickListener(this);
        navigate_btn.setOnClickListener(this);
        tab_home.setOnClickListener(this);
        tab_message.setOnClickListener(this);
        tab_user.setOnClickListener(this);
        tab_setting.setOnClickListener(this);

        initTabMenuSelected();

        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    public void loadImages(){
        images.add("http://chuantu.biz/t6/337/1530513364x-1566688664.jpg");
        images.add("http://chuantu.biz/t6/337/1530513397x-1566688664.jpg");
        images.add("http://chuantu.biz/t6/337/1530513420x-1566688664.jpg");
    }

    public void initTabMenuSelected(){
        tab_home.setSelected(true);
        tab_message.setSelected(false);
        tab_user.setSelected(false);
        tab_setting.setSelected(false);
    }

    public void resetTabMenuSelected(){
        tab_home.setSelected(false);
        tab_message.setSelected(false);
        tab_user.setSelected(false);
        tab_setting.setSelected(false);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.reserve_btn:
                Intent reserveIntent = new Intent(MainActivity.this, AppointNaviActivity.class);
                startActivity(reserveIntent);
                break;
            case R.id.record_btn:
                break;
            case R.id.position_btn:
                break;
            case R.id.schedule_btn:
                Intent scheduleIntent = new Intent(MainActivity.this, LineActivity.class);
                startActivity(scheduleIntent);
                break;
            case R.id.map_btn:
                break;
            case R.id.navigate_btn:
                break;
            case R.id.login_btn:
//                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(loginIntent);
                  UMSGUI.showLogin(new OperationCallback<User>(){
                    public void onSuccess(User user) {

                        // 登录成功的操作
                    }

                    public void onCancel() {
                        // 执行取消的操作
                    }

                    public void onFailed(Throwable t) {
                        // 提示错误信息
                    }
                });
                break;
            case R.id.register_btn:
                break;
            case R.id.tabmenu_home:
                resetTabMenuSelected();
                tab_home.setSelected(true);
                ToastUtils.showShort("home");
                break;
            case R.id.tabmenu_message:
                resetTabMenuSelected();
                tab_message.setSelected(true);
                ToastUtils.showShort("message");
                break;
            case R.id.tabmenu_user:
                resetTabMenuSelected();
                tab_user.setSelected(true);
                ToastUtils.showShort("user");
                break;
            case R.id.tabmenu_setting:
                resetTabMenuSelected();
                tab_setting.setSelected(true);
                ToastUtils.showShort("setting");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigation_item_message:
                Intent message_intent = new Intent(MainActivity.this,MessageActivity.class);
                startActivity(message_intent);
                break;
            case R.id.navigation_item_idea:
                FeedbackAgent agent = new FeedbackAgent(App.getInstance());
                agent.startDefaultThreadActivity();
                break;
            default:
                break;
        }
        return true;
    }

    private void init(){
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        //判断当前版本在4.0以上并且存在虚拟按键，否则不做操作
        if (Build.VERSION.SDK_INT < 19 || !checkDeviceHasNavigationBar()) {
            //一定要判断是否存在按键，否则在没有按键的手机调用会影响别的功能。如之前没有考虑到，导致图传全屏变成小屏显示。
            return;
        } else {
            // 获取属性
            decorView.setSystemUiVisibility(flag);
        }
    }

    /**
     * 判断是否存在虚拟按键
     * @return
     */
    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }


//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        return false;
//    }
}
