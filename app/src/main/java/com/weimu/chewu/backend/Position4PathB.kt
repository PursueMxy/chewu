package com.weimu.chewu.backend

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

import com.amap.api.maps.model.LatLng
import com.weimu.chewu.backend.bean.base.BaseB

import java.io.Serializable

/**
 * Author:你需要一台永动机
 * Date:2018/5/5 15:51
 * Description:
 */
class Position4PathB : BaseB {

    constructor(lat: String, lng: String, speed: String, bearing: String, time: String) : super() {
        this.lat = lat
        this.lng = lng
        this.speed = speed
        this.bearing = bearing
        this.time = time
    }

    var lat = "" //纬度
    var lng = "" //经度
    var speed = ""//速度
    var bearing = ""//角速度
    var time = ""//时间

    fun toLatLng(): LatLng {
        val lat = this.lat.toDouble()
        val lng = this.lng.toDouble()
        return LatLng(lat, lng)
    }


}
