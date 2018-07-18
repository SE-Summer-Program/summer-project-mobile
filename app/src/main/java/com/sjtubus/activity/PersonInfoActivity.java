package com.sjtubus.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.User;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.utils.ZxingUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener{
    private User user;
    private TextView username;
    private TextView isteacher;
    private TextView phone;
    private TextView realname;
    private TextView credit;
    private TextView studentnum;
    private ImageView qrcode;
    private Button logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initView();
        initUser();
    }

    public void initView(){
        username = findViewById(R.id.person_username);
        isteacher = findViewById(R.id.person_isteacher);
        phone = findViewById(R.id.person_phone);
        credit = findViewById(R.id.person_credit);
        realname = findViewById(R.id.person_realname);
        studentnum = findViewById(R.id.person_studentnum);
        logout_btn = findViewById(R.id.btn_logout);
        qrcode = findViewById(R.id.user_qrcode);
        logout_btn.setOnClickListener(this);
    }

    public void initUser(){
        user = UserManager.getInstance().getUser();
        username.setText(user.getUsername());
        isteacher.setText(user.getTeacher()?"教工":"非教工");
        phone.setText(user.getPhone());
        credit.setText(String.valueOf(user.getCredit()));
        realname.setText(user.getRealname());
        studentnum.setText(user.getStudentNumber());
        qrcode.setImageBitmap(ZxingUtils.createQRImage(user.getUsername(),250,250));

    }

    @Override
    public int getContentViewId(){
        return R.layout.activity_personinfo;
    }

    public void userLogout(){
        RetrofitClient
            .getBusApi()
            .logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<HttpResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(HttpResponse response) {
                    if(response.getError()==0){
                        UserManager.getInstance().logout();
                        ToastUtils.showShort("已退出登录~");
                        finish();
                    }else {
                        ToastUtils.showShort(response.getError());
                    }
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

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_logout:
                userLogout();
                break;
        }
    }
}
