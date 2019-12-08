package com.weimu.gmap.core.location;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.orhanobut.logger.Logger;
import com.weimu.universalib.utils.EventBusPro;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author:你需要一台永动机
 * Date:2018/5/9 18:12
 * Description:
 */
public class LocationCenter implements AMapLocationListener {

    private Context context;


    private static final LocationCenter ourInstance = new LocationCenter();

    public static LocationCenter getInstance() {
        return ourInstance;
    }


    private AMapLocationClient mLocationClient = null;//AMapLocationClient类对象

    private LocationCenter() {

    }


    private AMapLocation currentPosition;


    private AMapLocationClientOption normalLocationOption = normalLocationOption();
    private AMapLocationClientOption onceLocationOption = onceLocationOption();

    public AMapLocation getCurrentPosition() {
        return currentPosition;
    }


    //开启服务 手动
    public void startLocation() {
        mLocationClient.startLocation();
    }

    /**
     * 0为 普通
     * 1为 单次
     *
     * @param type
     */
    public void startLocation(int type) {
        if (type == 0) {
            startLocation(normalLocationOption);
        } else {
            startLocation(onceLocationOption);
        }
    }

    //开启服务 手动 带配置
    public void startLocation(AMapLocationClientOption option) {
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    //关闭服务 手动
    public void stopLocation() {
        mLocationClient.stopLocation();
    }

    public void init(Context context) {
        this.context = context;
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        startLocation(0);
    }

    //普通定位配置
    private AMapLocationClientOption normalLocationOption() {
        //选项
        AMapLocationClientOption option = new AMapLocationClientOption();
        //是否使用传感器
        option.setSensorEnable(true);
        //出行模式
        //option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        //高精度
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //定位间隔时间
        option.setInterval(2000);
        //是否为单词定位
        option.setOnceLocationLatest(false);
        return option;
    }

    //单词定位配置
    private AMapLocationClientOption onceLocationOption() {
        //选项
        AMapLocationClientOption option = new AMapLocationClientOption();
        //是否使用传感器
        option.setSensorEnable(true);
        //出行模式
        //option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        //高精度
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //是否为单词定位
//        option.setOnceLocation(true);
        option.setOnceLocationLatest(true);

        return option;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                sendPostion(amapLocation);
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Logger.e("location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
//                Toast.makeText(context, amapLocation.getErrorInfo(), Toast.LENGTH_LONG).show();
            }
        }


    }

    //发送位置
    private void sendPostion(AMapLocation amapLocation) {
        //todo 手动设置经纬度
        //amapLocation.setLatitude(24.513627);
        //amapLocation.setLongitude(118.125912);


        currentPosition = amapLocation;

        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
        amapLocation.getLatitude();//获取纬度
        amapLocation.getLongitude();//获取经度
        amapLocation.getAccuracy();//获取精度信息
        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
        amapLocation.getCountry();//国家信息
        amapLocation.getProvince();//省信息
        amapLocation.getCity();//城市信息
        amapLocation.getDistrict();//城区信息
        amapLocation.getStreet();//街道信息
        amapLocation.getStreetNum();//街道门牌号信息
        amapLocation.getCityCode();//城市编码
        amapLocation.getAdCode();//地区编码
        amapLocation.getAoiName();//获取当前定位点的AOI信息
        amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
        amapLocation.getFloor();//获取当前室内定位的楼层
        amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
        //获取定位时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(amapLocation.getTime());
        df.format(date);

        //发送定位的位置
        EventBusPro.post(currentPosition);
    }

    //必须在app要退出的界面 执行此销毁命令
    public void destroy() {
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    //转换
    public static LatLng amapLocation2LatLng(AMapLocation amapLocation) {
        return new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
    }

    //定位是否开启
    public boolean locationIsStart() {
        return mLocationClient != null && mLocationClient.isStarted();
    }
}
