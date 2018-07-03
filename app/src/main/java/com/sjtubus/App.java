package com.sjtubus;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

//import com.avos.avoscloud.AVException;
//import com.avos.avoscloud.AVInstallation;
//import com.avos.avoscloud.AVOSCloud;
//import com.avos.avoscloud.PushService;
//import com.avos.avoscloud.SaveCallback;
//import com.mcxiaoke.packer.helper.PackerNg;
import com.sjtubus.user.ReminderManager;
import com.sjtubus.user.UserChangeEvent;
import com.sjtubus.user.UserManager;
//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;
//import com.tencent.bugly.Bugly;
//import com.tencent.bugly.crashreport.CrashReport;
//import com.tencent.stat.StatConfig;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Allen on 2018/7/3.
 */

public class App extends Application {

    @SuppressWarnings("StaticFieldLeak")
    private static Context context;
    final private static String SharedPrefsCookie = "buscookie";
    final private static String SharedPrefsReminder = "busreminder";
    final private static String AppVersionName = "1.1";

    private String installationId;

    public String getInstallationId() {
        return installationId;
    }

    public static Context getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //setupLeakCanary();
        context = this;
        EventBus.getDefault().register(this);
        UserManager.init();
        //initBugly();
        //initLeanCloud();
        //initMta();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initReminderManager(UserChangeEvent event) {
        if (UserManager.getInstance().isLogin()) {
            ReminderManager manager = new ReminderManager();
            manager.synchronize();
        }
    }

//    private void initBugly() {
//        final String buglyId = "900018050";
//        final Boolean debug = false;
//        Bugly.init(getApplicationContext(), buglyId, debug);
//        CrashReport.setAppVersion(getApplicationContext(), AppVersionName);
//    }

    public static SharedPreferences getSharedPrefsCookie() {
        return context.getSharedPreferences(SharedPrefsCookie, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPrefsReminder() {
        return context.getSharedPreferences(SharedPrefsReminder, Context.MODE_PRIVATE);
    }

    public String getUserAgent() {
        String packageName = context.getPackageName();
        String versionName;
        try {
            versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "UNKNOWN";
        }
        String userAgent = "Android" + "/" + versionName;
        return userAgent;
    }

//    private void initLeanCloud() {
//        final String appID = "mSU7LJ8jBeRMRkkXVNNuseml-gzGzoHsz";
//        final String appKey = "MxxboYqkTenaqlWz3BRfoDG7";
//        AVOSCloud.initialize(this, appID, appKey);
//
//        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
//            public void done(AVException e) {
//                if (e == null) {
//                    // 保存成功
//                    installationId = AVInstallation.getCurrentInstallation().getInstallationId();
//                    // 关联  installationId 到用户表等操作……
//                } else {
//                    // 保存失败，输出错误信息
//                }
//            }
//        });
//        PushService.setDefaultPushCallback(this, LaunchActivity.class);
//        // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
//        PushService.subscribe(this, "public", LaunchActivity.class);
//    }

//    protected RefWatcher setupLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return RefWatcher.DISABLED;
//        }
//        return LeakCanary.install(this);
//    }

//    protected void initMta() {
//        String channel = PackerNg.getChannel(getApplicationContext());
//        if (channel == null) channel = "";
//        StatConfig.setInstallChannel(channel);
//    }
}
