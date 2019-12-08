package com.weimu.universalib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.weimu.universalib.interfaces.MyActivityLifeCycleCallbacks;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Author:你需要一台永动机
 * Date:2018/4/11 23:48
 * Description:
 */
public class OriginAppData extends Application {


    private static final String LOGGER_TAG = "weimu";

    private static Context mContext;
    //维护Activity 的list
    private static Stack<Activity> activityList = new Stack<>();

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initLog();
        registerActivityLifeCallback();
        // 添加中文城市词典
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(mContext)));
    }



    private void initLog() {
        PrettyFormatStrategy weimu = PrettyFormatStrategy.newBuilder().showThreadInfo(false)
                .methodCount(1)
                .tag(LOGGER_TAG)
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(weimu) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }


    //获取APP级别的上下文
    public static Context getContext() {
        return mContext;
    }


    public static Activity getCurrentActivity() {
        Activity target = null;
        try {
            target = activityList.peek();
        } catch (EmptyStackException e) {
            target = null;
        }
        return target;
    }

    /**
     * 关闭所有Activity
     */
    public static void closeAllActivity() {
        for (Activity ac : activityList) {
            if (ac != null) {
                ac.finish();
//                ac.overridePendingTransition(0, R.anim.slide_right_out);
            }
        }
    }

    /**
     * 描述：注册界面的生命周期，也可以
     */
    private void registerActivityLifeCallback() {
        registerActivityLifecycleCallbacks(new MyActivityLifeCycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityList.add(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityList.remove(activity);
            }
        });
    }
}
