package com.weimu.chewu.backend;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.WalkPath;
import com.weimu.chewu.backend.bean.PositionB;
import com.weimu.chewu.backend.bean.base.BaseB;

/**
 * Author:你需要一台永动机
 * Date:2018/5/5 22:35
 * Description:
 */
public class PathPlanItemB extends BaseB {
    private PositionB position;//位置
    private String time;//时间
    private String distance;//距离
    private int trafficeLight = 0;//交通
    private DrivePath drivePath;//驾驶路径
    private WalkPath walkPath;//步行路径
    private RidePath ridePath;//骑行
    private BusPath busPath;//公交


    public RidePath getRidePath() {
        return ridePath;
    }

    public void setRidePath(RidePath ridePath) {
        this.ridePath = ridePath;
    }

    public WalkPath getWalkPath() {
        return walkPath;
    }

    public void setWalkPath(WalkPath walkPath) {
        this.walkPath = walkPath;
    }

    public PositionB getPosition() {
        return position;
    }

    public void setPosition(PositionB position) {
        this.position = position;
    }

    public String getTime() {
        return time;
    }

    public void setTime(long time) {
        //把秒 变成 分钟 小时
        if (time < 60) {
            this.time = time + "秒";
        } else if (time < 60 * 60) {
            this.time = (time / 60) + "分钟";
        } else if (time < 60 * 60 * 24) {
            int hour = (int) (time / 60 / 60);
            int minute = (int) ((time / 60) % 60);
            this.time = hour + "小时" + minute + "分钟";
        }
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getTrafficeLight() {
        return trafficeLight;
    }

    public void setTrafficeLight(int trafficeLight) {
        this.trafficeLight = trafficeLight;
    }

    public DrivePath getDrivePath() {
        return drivePath;
    }

    public void setDrivePath(DrivePath drivePath) {
        this.drivePath = drivePath;
    }


    public BusPath getBusPath() {
        return busPath;
    }

    public void setBusPath(BusPath busPath) {
        this.busPath = busPath;
    }
}
