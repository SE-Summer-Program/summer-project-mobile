package com.sjtubus.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.network.cookie.BusCookieJar;
import com.sjtubus.user.UserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Allen on 2018/7/15.
 */

public class JaccountActivity extends BaseActivity {

    private WebView auth_webview;

    private String redirect_url;
    private String auth_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redirect_url = getString(R.string.sjtubus_host)+"/auth/redirect";
        auth_url = getString(R.string.sjtubus_host)+"/auth/jaccount";
        initWebview();
    }

    @Override
    public int getContentViewId(){
        return R.layout.activity_jaccount;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebview() {
        auth_webview = findViewById(R.id.webview_auth);
        auth_webview.getSettings().setJavaScriptEnabled(true);
        auth_webview.getSettings().setUserAgentString(((App) App.getInstance()).getUserAgent());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {

            }
        });
        String cookieString = loadCookies();
        cookieManager.setCookie(auth_url, cookieString);
        auth_webview.setWebViewClient(new AuthWebClient());
        auth_webview.loadUrl(auth_url);
    }

    private String loadCookies() {
        BusCookieJar cookieJar = BusCookieJar.getInstance();
        return cookieJar.getCookieString(HttpUrl.parse(auth_url));
    }

    class AuthWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("JaccountActivity", "Start loading: " + url);

            List<String> successList = Arrays.asList(
                    redirect_url
            );

            if (!successList.contains(url)) {
                return;
            }
            view.stopLoading();
            saveCookies();
            UserManager.getInstance().refresh();
            finish();
        }

        private void saveCookies() {
            CookieManager cookieManager = CookieManager.getInstance();
            BusCookieJar cookieJar = BusCookieJar.getInstance();

            HttpUrl authUrl = HttpUrl.parse(auth_url);

            String[] cookieStrings = cookieManager.getCookie(auth_url).split(";");

            List<Cookie> cookies = new ArrayList<>(cookieStrings.length);
            for (String cookieString : cookieStrings) {
                Cookie cookie = Cookie.parse(authUrl, cookieString);
                cookies.add(cookie);
            }
            cookieJar.clear(); // prevent duplicated cookie names
            cookieJar.saveFromResponse(authUrl, cookies);
        }
    }

    @Override
    public void onBackPressed() {
        if (auth_webview.canGoBack()) {
            auth_webview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
