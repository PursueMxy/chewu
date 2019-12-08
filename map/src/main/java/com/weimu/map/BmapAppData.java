package com.weimu.map;

import android.app.Application;

import com.weimu.map.location.LocationCenter;
import com.weimu.map.map.MapCenter;

/**
 * Author:你需要一台永动机
 * Date:2018/4/22 00:21
 * Description:
 */
public class BmapAppData extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MapCenter.getInstance().init(this);
        LocationCenter.getInstance().init(this);
    }
}
