package com.weimu.chewu.origin.center

import android.content.Context
import com.umeng.commonsdk.UMConfigure

/**
 * Author:你需要一台永动机
 * Date:2018/4/11 10:27
 * Description:
 */
object UmengCenter {
    //Umeng
    private val UMENG_APPKEY = "5b8ea83db27b0a4fcb0000d1"
    private val UMENG_MESSAGE_SECRECT = ""





    fun init(context: Context, channel: String = "uMeng") {
        //初始化友盟
        UMConfigure.init(context,
                UMENG_APPKEY,
                channel,
                UMConfigure.DEVICE_TYPE_PHONE,
                UMENG_MESSAGE_SECRECT)


    }





}