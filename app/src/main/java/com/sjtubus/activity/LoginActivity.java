package com.sjtubus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

/**
 * Created by Allen on 2018/7/3.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    EditText phone_edit,password_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initViews();
    }

    public void initViews(){
        TextView register_txt = findViewById(R.id.txt_register);
        Button login_btn = findViewById(R.id.btn_login);
        TextView jaccount_btn = findViewById(R.id.jaccount_btn);
        phone_edit = findViewById(R.id.login_phone_edit);
        password_edit =findViewById(R.id.login_pwd_edit);
        jaccount_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        register_txt.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar_login);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public int getContentViewId(){
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.txt_register:
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.btn_login:
                userLogin();
                break;
            case R.id.jaccount_btn:
                Intent jaccountIntent = new Intent(LoginActivity.this,JaccountActivity.class);
                startActivity(jaccountIntent);
                break;
            case R.id.txt_forget_password:
                ToastUtils.showShort("忘记密码功能还没有做好哦~");
                break;
        }
    }

    public void userLogin(){
        String phone = phone_edit.getText().toString().trim();
        String password = password_edit.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.showShort("手机号码为空!");
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtils.showShort("密码不能为空!");
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("password", password)
                .add("phone",phone).build();
        //登录
        RetrofitClient.getBusApi()
                .login(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(HttpResponse response) {
                        if(response.getError()==0){
                            //更新用户信息
                            UserManager.getInstance().refresh();
                            finish();
                        }
                        ToastUtils.showShort(response.getMsg());
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
}

