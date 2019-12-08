package com.weimu.chewu.origin.center;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.weimu.chewu.AppData;
import com.weimu.chewu.BuildConfig;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.PositionB;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.UserInfoB;
import com.weimu.chewu.backend.http.core.cookie.CookieCenter;
import com.weimu.chewu.module.loginx.LoginActivity;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.MODE_PRIVATE;

/**
 * Author:你需要一台永动机
 * Date:2018/3/23 09:34
 * Description:
 */

public class UserCenter {
    //单例模式
    private volatile static UserCenter INSTANCE;//声明 volatiel 禁止指令重排序优化


    private UserCenter() {
    }

    public static UserCenter getInstance() {
        return UserCenter.Holder.INSTANCE;
    }

    private static class Holder {
        static UserCenter INSTANCE = new UserCenter();

    }

    /**
     * User SharePreferences
     */
    private static final String SHARE_PREFERENCE_USER = "share_preference_user";

    public UserB getUserShareP() {
        SharedPreferences sp = AppData.getContext().getSharedPreferences(BuildConfig.APPNAME, MODE_PRIVATE);
        String jsonConfig = sp.getString(SHARE_PREFERENCE_USER, "");
        return new Gson().fromJson(jsonConfig, UserB.class);
    }

    public void setUserShareP(UserB appSharePre) {
        SharedPreferences sp = AppData.getContext().getSharedPreferences(BuildConfig.APPNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SHARE_PREFERENCE_USER, new Gson().toJson(appSharePre));
        editor.apply();
    }

    public void cleanUserShareP() {
        SharedPreferences sp = AppData.getContext().getSharedPreferences(BuildConfig.APPNAME, MODE_PRIVATE);
        sp.edit().remove(SHARE_PREFERENCE_USER).apply();
    }


    //注销
    public void logOut() {
        CookieCenter.getInstance().clearAllCookie();
        cleanUserShareP();
        Activity currentActivity = AppData.getCurrentActivity();
        currentActivity.startActivity(LoginActivity.newIntent(currentActivity));
        // 调用 JPush 接口来设置别名。
        JPushInterface.deleteAlias(AppData.getContext(),1);
    }


    //用户是否登录
    public boolean isUserLogin() {
        return getUserShareP() != null;
    }


    //需要登录
    public boolean needLogin() {
        if (!isUserLogin()) {
            openLoginPage();
            return true;
        }
        return false;
    }

    public void shouldLogin() {
        logOut();
        openLoginPage();
    }

    //打开登录界面
    private void openLoginPage() {

        Activity currentActivity = AppData.getCurrentActivity();
        currentActivity.startActivity(LoginActivity.newIntent(currentActivity));
        currentActivity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.noting);
    }

}
