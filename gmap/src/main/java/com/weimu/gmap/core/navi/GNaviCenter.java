package com.weimu.gmap.core.navi;

import android.content.Context;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.orhanobut.logger.Logger;

/**
 * Author:你需要一台永动机
 * Date:2018/5/5 13:23
 * Description:
 */
public class GNaviCenter  {
    private static final GNaviCenter ourInstance = new GNaviCenter();

    public static GNaviCenter getInstance() {
        return ourInstance;
    }

    private GNaviCenter() {

    }

    private AMapNavi mAMapNavi;

    public void init(Context context) {

    }




}
