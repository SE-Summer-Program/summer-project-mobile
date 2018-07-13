package com.sjtubus.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.network.cookie.BusCookieJar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Allen on 2018/7/3.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    WebView webview;

    String busUrlString, loginUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initViews();
        busUrlString = getString(R.string.sjtubus_host);
        loginUrl = getString(R.string.sjtubus_host) + "/user/login";
    }

    public void initViews(){
        TextView register_btn = findViewById(R.id.txt_register);
        register_btn.setOnClickListener(this);
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

        }
    }

//    private void initWebview() {
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setUserAgentString(((App) App.getInstance()).getUserAgent());
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
//            @Override
//            public void onReceiveValue(Boolean value) {
//
//            }
//        });
//        Schedule cookieString = loadCookies();
//        cookieManager.setCookie(busUrlString, cookieString);
//        webview.setWebViewClient(new LoginWebClient());
//        webview.loadUrl(loginUrl);
//    }
//
//    private Schedule loadCookies() {
//        BusCookieJar cookieJar = BusCookieJar.getInstance();
//        Schedule cookies = cookieJar.getCookieString(HttpUrl.parse(busUrlString));
//        return cookies;
//    }
//
//    class LoginWebClient extends WebViewClient {
//
//        @Override
//        public void onPageStarted(WebView view, Schedule url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            Log.d("LoginActivity", "Start loading: " + url);
//
//            List<Schedule> successList = Arrays.asList(
//                    busUrlString,
//                    busUrlString + '/',
//                    busUrlString + "/index.html"
//            );
//
//            if (!successList.contains(url)) {
//                return;
//            }
//
//            view.stopLoading();
//            saveCookies();
//            loadData();
//        }
//
//        private void saveCookies() {
//            CookieManager cookieManager = CookieManager.getInstance();
//            BusCookieJar cookieJar = BusCookieJar.getInstance();
//
//            HttpUrl busUrl = HttpUrl.parse(busUrlString);
//
//            Schedule[] cookieStrings = cookieManager.getCookie(busUrlString).split(";");
//
//            List<Cookie> cookies = new ArrayList<>(cookieStrings.length);
//            for (Schedule cookieString : cookieStrings) {
//                Cookie cookie = Cookie.parse(busUrl, cookieString);
//                cookies.add(cookie);
//            }
//            cookieJar.clear(); // prevent duplicated cookie names
//            cookieJar.saveFromResponse(busUrl, cookies);
//        }
//
//        private void loadData() {
////            addSubscription(RetrofitClient.getTongquApi()
////                    .getProfile()
////                    .flatMap(NetworkErrorHandler.tongquErrorFilter)
////                    .subscribeOn(Schedulers.io())
////                    .observeOn(AndroidSchedulers.mainThread())
////                    .subscribe(
////                            response -> {
////                                User user = ((UserProfileResponse) response).getResult();
////                                UserManager.getInstance().login(user);
////                                ToastUtils.showLong(R.string.login_success);
////                                finish();
////                            },
////                            NetworkErrorHandler.basicErrorHandler
////                    ));
//        }
//    }
}

