package com.sjtubus.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.feedback.FeedbackAgent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.model.User;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserChangeEvent;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.GlideImageLoader;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.MarqueeViewAdapter;
import com.stx.xmarqueeview.XMarqueeView;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class MainActivity extends BaseActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private TextView username;
    private TextView userinfo;
    private TextView login_tips;
    private TextView login_txt;
    private TextView register_txt;
    private XMarqueeView billboard;

    //private View decorView;

    private List<String> images = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Banner banner = findViewById(R.id.banner);
        initView();
        loadImages();
        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();

        //获取顶层视图
        //decorView = getWindow().getDecorView();
        //decorView = getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public int getContentViewId(){
        return R.layout.activity_main;
    }

    public void initView(){
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.person);

        Button reserve_btn = findViewById(R.id.reserve_btn);
        Button scan_btn = findViewById(R.id.scan_btn);
        Button position_btn = findViewById(R.id.position_btn);
        Button schedule_btn = findViewById(R.id.schedule_btn);
        Button map_btn = findViewById(R.id.map_btn);
        Button navigate_btn = findViewById(R.id.navigate_btn);

        reserve_btn.setOnClickListener(this);
        scan_btn.setOnClickListener(this);
        position_btn.setOnClickListener(this);
        schedule_btn.setOnClickListener(this);
        map_btn.setOnClickListener(this);
        navigate_btn.setOnClickListener(this);

        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout nav_header_layout = (LinearLayout) navigationView.getHeaderView(0);
        username = nav_header_layout.findViewById(R.id.username_txt);
        userinfo = nav_header_layout.findViewById(R.id.shortinfo_txt);
        login_tips = nav_header_layout.findViewById(R.id.login_tips);
        login_txt = nav_header_layout.findViewById(R.id.login_txt);
        register_txt = nav_header_layout.findViewById(R.id.register_txt);
        //设置下划线
        login_txt.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        register_txt.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );

        //设置滚动轮播
        billboard = findViewById(R.id.billboard);
        messages.add("2018.8.19即明日,校园巴士停运一天!");
        messages.add("好消息！校车时速已达到100km/S!");
        messages.add("恭喜学号为2333的同学喜提校车一辆!");
        MarqueeViewAdapter billboard_adapter = new MarqueeViewAdapter(messages, this);
        //刷新公告
        //billboard_adapter.setData(messages);
        billboard.setAdapter(billboard_adapter);
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
            if(UserManager.getInstance().getRole().equals("driver")){
                role = "司机";
            }else if(UserManager.getInstance().getRole().equals("admin")){
                role = "管理员";
            }
            String userinfo_str = "身份:"+role+"   "+"信用积分:"+user.getCredit();
            userinfo.setText(userinfo_str);
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
                if(! UserManager.getInstance().getRole().equals("user")){
                    ToastUtils.showShort("抱歉~预约请用乘客账号哦~");
                    break;
                }
                Intent reserveIntent = new Intent(MainActivity.this, AppointNaviActivity.class);
                startActivity(reserveIntent);
                break;
            case R.id.scan_btn:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                if(UserManager.getInstance().getRole().equals("user")){
                    ToastUtils.showShort("抱歉~您没有管理员权限哦~");
                    break;
                }
                new IntentIntegrator(this)
                        .setOrientationLocked(false)
                        .setCaptureActivity(SimpleScanActivity.class) // 设置自定义的activity是CustomActivity
                        .initiateScan(); // 初始化扫描
                break;
            case R.id.position_btn:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                if(UserManager.getInstance().getRole().equals("user")){
                    ToastUtils.showShort("抱歉~您没有司机权限哦~");
                    break;
                }
                Intent positionIntent = new Intent(MainActivity.this, GPSPositionActivity.class);
                startActivity(positionIntent);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigation_item_person:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                Intent person_intent = new Intent(MainActivity.this,PersonInfoActivity.class);
                startActivity(person_intent);
                break;
            case R.id.navigation_item_reserve:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                Intent recordIntent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(recordIntent);
                break;
            case R.id.navigation_item_collect:
                Intent collectIntent = new Intent(MainActivity.this, CollectActivity.class);
                startActivity(collectIntent);
                break;
            case R.id.navigation_item_message:
                Intent messageIntent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(messageIntent);
                break;
            case R.id.navigation_item_idea:
                FeedbackAgent agent = new FeedbackAgent(App.getInstance());
                agent.startDefaultThreadActivity();
                break;
            case R.id.navigation_item_setting:
                Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(settingIntent);
                break;
            default:
                break;
        }
        return true;
    }

//    private void init(){
//        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
//                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        //判断当前版本在4.0以上并且存在虚拟按键，否则不做操作
//        if (!checkDeviceHasNavigationBar()) {
//            //一定要判断是否存在按键，否则在没有按键的手机调用会影响别的功能。如之前没有考虑到，导致图传全屏变成小屏显示。
//        } else {
//            // 获取属性
//            decorView.setSystemUiVisibility(flag);
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        billboard.startFlipping();
        UserManager.getInstance().refresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        billboard.stopFlipping();
    }

    /**
     * 判断是否存在虚拟按键
     * @ return
     */
    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            /*
             * 不是很清楚这个注解加了是干嘛的
             */
            @SuppressLint("PrivateApi") Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("扫描失败!")
                        .setContentText("二维码内容为空!")
                        .setConfirmText("确定")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
            } else {
                // ScanResult 为 获取到的字符串
                String scanResult = intentResult.getContents();
                String[] info = scanResult.split(";");
                if(info.length < 3){
                    ToastUtils.showShort("二维码格式不正确哦~");
                    return;
                }
                RequestBody requestBody = new FormBody.Builder()
                        .add("username", info[2])
                        .add("shift_id",info[0])
                        .add("departure_date",info[1])
                        .build();
                //登录
                RetrofitClient.getBusApi()
                        .verifyUser(requestBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<HttpResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(HttpResponse response) {
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("验证完成!")
                                        .setContentText(response.getMsg())
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");
                                //mProgressBar.setVisibility(View.GONE);
                            }
                        });
            }
        } else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
