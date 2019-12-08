package com.weimu.map.map;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Author:你需要一台永动机
 * Date:2018/4/7 23:45
 * Description:
 */
public class MapCenter {
    private static final MapCenter ourInstance = new MapCenter();

    public static MapCenter getInstance() {
        return ourInstance;
    }

    private MapCenter() {

    }

    public void init(Context context) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(context);
    }
}
