package com.weimu.chewu.backend.http.core.cookie;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.orhanobut.logger.Logger;
import com.weimu.chewu.AppData;

import java.util.Arrays;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;


public class CookieCenter {

    private PersistentCookieStore cookieStore = new PersistentCookieStore(AppData.getContext());

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }

    private CookieCenter() {
    }

    public static CookieCenter getInstance() {
        return CookieCenter.Holder.INSTANCE;
    }

    public static class Holder {
        public static CookieCenter INSTANCE = new CookieCenter();

    }


    //加载cookie
    public void loadCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();

        List<Cookie> cookies = cookieStore.getCookies();
        StringBuilder builder = new StringBuilder();
        builder.append("WebView读取 cookies数=" + cookies.size() + "\n");
        for (Cookie item : cookies) {
            cookieManager.setCookie(url, item.name() + "=" + item.value());
            builder.append(item.name() + "=" + item.value() + "\n");
        }
        Logger.d(builder.toString());

        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }

    //存储cookie
    public void saveCookie(Context context, final String url) {

        // //获取webview里面的cookie信息
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String cookieStr = cookieManager.getCookie(url);

        if (TextUtils.isEmpty(cookieStr)) return;
        List<String> cookies = Arrays.asList(cookieStr.split(";"));
        HttpUrl parseUrl = HttpUrl.parse(url);

        if (cookies == null || cookies.size() <= 0) return;
        StringBuilder builder = new StringBuilder();
        builder.append("WebView保存 cookies数=" + cookies.size() + "\n");
        for (String cookie : cookies) {
            Cookie item = Cookie.parse(parseUrl, cookie);
            cookieStore.add(parseUrl, item);
            builder.append(item.name()).append("=").append(item.value()).append("\n");
        }
        Logger.d(builder.toString());
    }

    //清除所有Cookie
    public void clearAllCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
    }
}
