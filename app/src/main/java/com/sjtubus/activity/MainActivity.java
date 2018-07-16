package com.sjtubus.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.feedback.FeedbackAgent;
import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.model.User;
import com.sjtubus.user.UserChangeEvent;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.GlideImageLoader;
import com.sjtubus.utils.ToastUtils;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends BaseActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mToolbar;
    private Button reserve_btn;
    private Button record_btn;
    private Button position_btn;
    private Button schedule_btn;
    private Button map_btn;
    private Button navigate_btn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayout nav_header_layout;
    private TextView username;
    private TextView userinfo;
    private TextView login_tips;
    private TextView login_txt;
    private TextView register_txt;


    private TextView tab_home;
    private TextView tab_message;
    private TextView tab_user;
    private TextView tab_setting;

    private View decorView;

    private List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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

        nav_header_layout = (LinearLayout) navigationView.getHeaderView(0);
        username = nav_header_layout.findViewById(R.id.username_txt);
        userinfo = nav_header_layout.findViewById(R.id.shortinfo_txt);
        login_tips = nav_header_layout.findViewById(R.id.login_tips);
        login_txt = nav_header_layout.findViewById(R.id.login_txt);
        register_txt = nav_header_layout.findViewById(R.id.register_txt);
        login_txt.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        register_txt.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
    }
    public void loadImages(){
        //目前的图片是用的网上的传图网站
        images.add("http://chuantu.biz/t6/337/1530513364x-1566688664.jpg");
        images.add("http://chuantu.biz/t6/337/1530513397x-1566688664.jpg");
        images.add("http://chuantu.biz/t6/337/1530513420x-1566688664.jpg");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserStatChange(UserChangeEvent event) {
        Log.d("SJTUBUS", "User changed!");
        updateUser();
    }

    public void updateUser(){
        User user = UserManager.getInstance().getUser();
        boolean isLogin = UserManager.getInstance().isLogin();
        if (isLogin && user != null) {
            username.setText(user.getUsername());
            username.setVisibility(View.VISIBLE);
            String role = user.getTeacher()?"教工":"普通用户";
            userinfo.setText("身份:"+role+"   "+"信用积分:"+user.getCredit());
            userinfo.setVisibility(View.VISIBLE);
            login_tips.setVisibility(View.GONE);
            login_txt.setVisibility(View.GONE);
            register_txt.setVisibility(View.GONE);
        } else {
            login_tips.setVisibility(View.VISIBLE);
            login_txt.setVisibility(View.VISIBLE);
            register_txt.setVisibility(View.VISIBLE);
            username.setVisibility(View.GONE);
            userinfo.setVisibility(View.GONE);
            //Picasso.with(this).load(R.drawable.logo_grey).into(imageAvatar);
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.reserve_btn:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                Intent reserveIntent = new Intent(MainActivity.this, AppointActivity.class);
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
                Intent mapIntent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(mapIntent);
                break;
            case R.id.navigate_btn:
                Intent navigateIntent = new Intent(MainActivity.this,RouteActivity.class);
                startActivity(navigateIntent);
                break;
            case R.id.login_txt:
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.register_txt:
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
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
            case R.id.navigation_item_person:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                Intent person_intent = new Intent(MainActivity.this,PersonInfoActivity.class);
                startActivity(person_intent);
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

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
