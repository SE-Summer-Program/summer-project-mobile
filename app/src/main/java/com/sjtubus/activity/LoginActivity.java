package com.sjtubus.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;


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

public class LoginActivity extends BaseActivity {
    WebView webview;

    String busUrlString, loginUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        busUrlString = getString(R.string.sjtubus_host);
        loginUrl = getString(R.string.sjtubus_host) + "/user/login";
        initWebview();
    }

    private void initWebview() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUserAgentString(((App) App.getInstance()).getUserAgent());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {

            }
        });
        String cookieString = loadCookies();
        cookieManager.setCookie(busUrlString, cookieString);
        webview.setWebViewClient(new LoginWebClient());
        webview.loadUrl(loginUrl);
    }

    private String loadCookies() {
        BusCookieJar cookieJar = BusCookieJar.getInstance();
        String cookies = cookieJar.getCookieString(HttpUrl.parse(busUrlString));
        return cookies;
    }

    class LoginWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("LoginActivity", "Start loading: " + url);

            List<String> successList = Arrays.asList(
                    busUrlString,
                    busUrlString + '/',
                    busUrlString + "/index.html"
            );

            if (!successList.contains(url)) {
                return;
            }

            view.stopLoading();
            saveCookies();
            loadData();
        }

        private void saveCookies() {
            CookieManager cookieManager = CookieManager.getInstance();
            BusCookieJar cookieJar = BusCookieJar.getInstance();

            HttpUrl busUrl = HttpUrl.parse(busUrlString);

            String[] cookieStrings = cookieManager.getCookie(busUrlString).split(";");

            List<Cookie> cookies = new ArrayList<>(cookieStrings.length);
            for (String cookieString : cookieStrings) {
                Cookie cookie = Cookie.parse(busUrl, cookieString);
                cookies.add(cookie);
            }
            cookieJar.clear(); // prevent duplicated cookie names
            cookieJar.saveFromResponse(busUrl, cookies);
        }

        private void loadData() {
//            addSubscription(RetrofitClient.getTongquApi()
//                    .getProfile()
//                    .flatMap(NetworkErrorHandler.tongquErrorFilter)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            response -> {
//                                User user = ((UserProfileResponse) response).getResult();
//                                UserManager.getInstance().login(user);
//                                ToastUtils.showLong(R.string.login_success);
//                                finish();
//                            },
//                            NetworkErrorHandler.basicErrorHandler
//                    ));
        }


    }
}

