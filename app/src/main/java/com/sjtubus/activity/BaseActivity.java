package com.sjtubus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private CompositeDisposable compositeDisposable;


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
