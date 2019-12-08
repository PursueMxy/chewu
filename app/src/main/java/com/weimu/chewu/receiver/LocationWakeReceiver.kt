package com.weimu.chewu.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.weimu.chewu.services.UpLoadLocationService
import com.weimu.gmap.core.location.LocationCenter

/**
 * Author:你需要一台永动机
 * Date:2018/9/16 13:54
 * Description:熄屏定位唤醒接收者
 */
class LocationWakeReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val pm by lazy { context?.getSystemService(Context.POWER_SERVICE) as PowerManager }
        val wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        wl?.setReferenceCounted(false);
        //点亮屏幕
        wl?.acquire();
        //释放
        wl?.release();

        Log.d("LocationWakeReceiver", "定位重新获取");
        val locationIntent = Intent(context, UpLoadLocationService::class.java)
        context?.startService(locationIntent)

        //在这里重新申请定位
        LocationCenter.getInstance().startLocation(0)
    }
}