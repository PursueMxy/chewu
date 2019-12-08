package com.weimu.chewu.backend.bean

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
class PositionB : BaseB {
    var address: String? = null
    //纬度
    var latitude: Double = -1.0
        set(value) {
            field = value
            if (latitude > 0) isInit = true
        }
    //经度
    var longitude: Double = -1.0
        set(value) {
            field = value
            if (longitude > 0) isInit = true
        }
    var speed = 0f//速度
    var bearing = 0f//角速度
    var time: Long = 0//时间

    var accuracy:Float=0.0f//精度为米

    var locationType=-1//定位类型

    var province: String? = null
    var city: String? = null
    var isInit = false

    val isEmptyLocation: Boolean
        get() = latitude == -1.0 && longitude == -1.0

    constructor() {}

    constructor(address: String) {
        this.address = address
    }

    constructor(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }


    override fun toString(): String {
        return "($latitude,$longitude),seppd:$speed"
    }


    fun toStringV2(): String {
        return longitude.toString() + "," + latitude
    }

    override fun equals(obj: Any?): Boolean {
        if (obj !is PositionB) return false
        val target = obj as PositionB?
        return this.latitude == target!!.latitude && this.longitude == target.longitude
        //        return ((this.latitude + "").equals((target.latitude + "")) && ((this.longitude + "").equals((target.longitude + ""))));
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }


    fun toLatLng(): LatLng {
        return LatLng(this.latitude, this.longitude)
    }
}
