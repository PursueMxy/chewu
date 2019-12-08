package com.weimu.map.location;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Author:你需要一台永动机
 * Date:2018/4/7 22:37
 * Description:定位管理中心
 */
public class LocationCenter {

    private Context mContext;


    private static final LocationCenter ourInstance = new LocationCenter();

    public static LocationCenter getInstance() {
        return ourInstance;
    }

    private LocationCenter() {
    }

    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private LocationClientOption option = new LocationClientOption();


    //必须在Application中初始化
    public void init(Context context) {
        this.mContext = context;

        //定位客户端
        mLocationClient = new LocationClient(mContext);
        //注册监听者
        mLocationClient.registerLocationListener(myListener);
        //声明各种配置
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        //todo 可设置是否为定位1次
        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    //开始定位
    public void start() {
        if (mLocationClient == null)
            throw new IllegalStateException("mLocationClient must not null");
        mLocationClient.start();
    }

    //结束定位
    public void stop() {
        if (mLocationClient == null)
            throw new IllegalStateException("mLocationClient must not null");
        mLocationClient.stop();
    }

    //重新定位   用于在某些特定的异常环境下重启定位
    public void reStart() {
        if (mLocationClient == null)
            throw new IllegalStateException("mLocationClient must not null");
        mLocationClient.restart();
    }
}
