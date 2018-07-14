package com.sjtubus.user;

import android.util.Log;


import com.sjtubus.model.User;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.ProfileResponse;
import com.sjtubus.network.BusApi;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.ToastUtils;


import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;


/**
 * Created by Allen on 2018/7/3.
 */

public class UserManager {
    static private UserManager instance;
    private User user;
    private boolean login = false;

    private UserManager() {
    }

    public static UserManager getInstance() {
        return instance;
    }

    public static void init() {
        instance = new UserManager();
        instance.refresh();
    }

    public boolean isLogin() {
        return login;
    }

    public User getUser() {
        return user;
    }

    public void login(User user) {
        this.user = user;
        login = true;
        Log.d("EventBus", "UserChange true");
        ToastUtils.showShort("已登陆~");
        EventBus.getDefault().post(new UserChangeEvent(true));
    }

    public void logout() {
        user = null;
        login = false;
        Log.d("EventBus", "UserChange false");
        EventBus.getDefault().post(new UserChangeEvent(false));
    }

    public void refresh() {
        RetrofitClient
            .getBusApi()
            .getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ProfileResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(ProfileResponse response) {
                    if(response.getError()==0){
                        login(response.getUser());
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
}
