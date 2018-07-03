package com.sjtubus.user;

import android.util.Log;


import com.sjtubus.model.User;
import com.sjtubus.network.BusApi;
import com.sjtubus.network.RetrofitClient;


import org.greenrobot.eventbus.EventBus;


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
        EventBus.getDefault().post(new UserChangeEvent(true));
    }

    public void logout() {
        user = null;
        login = false;
        Log.d("EventBus", "UserChange false");
        EventBus.getDefault().post(new UserChangeEvent(false));
    }

    public void refresh() {
        BusApi api = RetrofitClient.getBusApi();
//        api.isLogin()
//                .flatMap(
//                        response -> {
//                            login = response.getError() == 0;
//                            return login ? api.getProfile() : Observable.never();
//                        }
//                )
//                .flatMap(NetworkErrorHandler.tongquErrorFilter)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        response -> {
//                            user = ((UserProfileResponse) response).getResult();
//                        },
//                        NetworkErrorHandler.basicErrorHandler
//                );
        EventBus.getDefault().post(new UserChangeEvent(true));
    }
}
