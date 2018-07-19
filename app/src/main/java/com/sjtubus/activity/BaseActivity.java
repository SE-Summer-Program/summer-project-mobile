package com.sjtubus.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.SDKInitializer;
import com.sjtubus.App;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(App.getInstance());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getContentViewId());
        App.translucentStatusBar(this,true);
    }

    private CompositeDisposable compositeDisposable;

    abstract protected int getContentViewId();

    public CompositeDisposable getCompositeDisposable() {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }

        return this.compositeDisposable;
    }


    public void addDisposable(Disposable s) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }

        this.compositeDisposable.add(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.compositeDisposable != null) {
            this.compositeDisposable.clear();
        }
    }
}
