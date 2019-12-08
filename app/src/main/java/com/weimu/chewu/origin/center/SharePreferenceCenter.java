package com.weimu.chewu.origin.center;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.weimu.chewu.AppData;
import com.weimu.chewu.BuildConfig;
import com.weimu.chewu.backend.bean.AppSharePreB;

import static android.content.Context.MODE_PRIVATE;


public class SharePreferenceCenter {

    //单例模式
    private volatile static SharePreferenceCenter INSTANCE;//声明 volatiel 禁止指令重排序优化


    private SharePreferenceCenter() {
    }

    public static SharePreferenceCenter getInstance() {
        return SharePreferenceCenter.Holder.INSTANCE;
    }

    private static class Holder {
        static SharePreferenceCenter INSTANCE = new SharePreferenceCenter();

    }


    /**
     * APP SharePreferences
     */
    private static final String SHARE_PREFERENCE_APP = "share_preference_app";

    //获取AppShareP
    public AppSharePreB getAppShareP() {
        SharedPreferences sp = AppData.getContext().getSharedPreferences(BuildConfig.APPNAME, MODE_PRIVATE);
        String jsonConfig = sp.getString(SHARE_PREFERENCE_APP, "");
        //特殊操作
        if (jsonConfig.equals("")) {
            AppSharePreB appSharePre = new AppSharePreB();
            setAppShareP(appSharePre);
            return appSharePre;
        }
        return new Gson().fromJson(jsonConfig, AppSharePreB.class);
    }

    //设置AppShareP
    public void setAppShareP(AppSharePreB appSharePre) {
        SharedPreferences sp = AppData.getContext().getSharedPreferences(BuildConfig.APPNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SHARE_PREFERENCE_APP, new Gson().toJson(appSharePre));
        editor.apply();
    }

    //清楚AppShareP
    public void cleanAppShareP() {
        SharedPreferences sp = AppData.getContext().getSharedPreferences(BuildConfig.APPNAME, MODE_PRIVATE);
        sp.edit().remove(SHARE_PREFERENCE_APP).apply();
    }


    /**
     * 存储-账号-密码
     */
    public void savePhonePwd(String username, String password) {
        AppSharePreB appSharePre = getAppShareP();
        if (appSharePre == null)
            return;
        //appSharePre.setUsername(username);
        //appSharePre.setPassword(password);
        setAppShareP(appSharePre);
    }




}
