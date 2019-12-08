package com.weimu.chewu.services

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMapUtils
import com.weimu.chewu.AppData
import com.weimu.chewu.R
import com.weimu.chewu.backend.bean.PositionB
import com.weimu.chewu.backend.http.observer.OnRequestObserver
import com.weimu.chewu.backend.remote.MainCase
import com.weimu.chewu.module.OnePixelActivity
import com.weimu.chewu.module.main.MainActivity
import com.weimu.chewu.module.main.MainCaseImpl
import com.weimu.chewu.origin.center.SharePreferenceCenter
import com.weimu.chewu.origin.center.UserCenter
import com.weimu.chewu.receiver.LocationWakeReceiver
import com.weimu.chewu.utils.NotificationHelper
import com.weimu.chewu.receiver.ScreenLockReceiver
import com.weimu.gmap.core.location.LocationCenter
import com.weimu.universalib.utils.EventBusPro
import com.weimu.universalib.utils.TimeUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 上传定位的服务
 */
class UpLoadLocationService : Service() {

    private val shareCenter: SharePreferenceCenter by lazy { SharePreferenceCenter.getInstance() }

    companion object {
        var currentPosition: PositionB = PositionB(-1.0, -1.0)
    }


    private lateinit var mCase: MainCase
    private var times = 0
    private var listener: ScreenLockReceiver? = null
    private val am by lazy { this.getSystemService(ALARM_SERVICE) as AlarmManager }
    private val locationWakeReceiver: LocationWakeReceiver = LocationWakeReceiver()
    private var pi: PendingIntent? = null



    override fun onCreate() {
        super.onCreate()
        initScreenListener()
        registerLocationWakeReceiver()
        //notification
        NotificationHelper.with(this)
        EventBus.getDefault().register(this)
        //初始化定位
        LocationCenter.getInstance().init(this)
        mCase = MainCaseImpl()

    }


    //注册唤醒cpu的接收者
    private fun registerLocationWakeReceiver() {
        val intentFile = IntentFilter()
        intentFile.addAction("locationWake")
        registerReceiver(locationWakeReceiver, intentFile)


        //写一个定时的Pendingintent
        val intent = Intent()
        intent.action = "locationWake"
        pi = PendingIntent.getBroadcast(this, 0, intent, 0)
    }

    private fun unregisterLocationWakeReceiver() {
        unregisterReceiver(locationWakeReceiver)
    }


    private fun wake() {
        LocationCenter.getInstance().stopLocation()

        // 每五秒唤醒一次
        val second = (15 * 1000).toLong()
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),  second, pi)
    }

    private fun unWake() {
        LocationCenter.getInstance().startLocation(0)
        am.cancel(pi);
    }

    private fun initScreenListener() {
        listener = ScreenLockReceiver(this)
        listener?.register(object : ScreenLockReceiver.ScreenStateListener {
            override fun onScreenOn() {
                sendBroadcast(Intent("finishPixel"))
                unWake()
            }

            override fun onScreenOff() {
                val intent = Intent(this@UpLoadLocationService, OnePixelActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                wake()

            }

            override fun onUserPresent() {
                Log.e("Screen", "Screen --> onUserPresent--> ")
            }
        })
    }

    private fun unInitScreenListener() {
        listener?.unregister()
    }


    fun startForegound() {
        val orderId = SharePreferenceCenter.getInstance().appShareP.orderId
        if (orderId == -1) return
        val context = AppData.getContext()

        val nfIntent = Intent(this, MainActivity::class.java)
        nfIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val nb = Notification.Builder(context)
                .setContentIntent(PendingIntent.getActivity(context, 0, nfIntent, 0))
                .setContentTitle("车务")
                .setContentText("订单跟踪中")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            nb.setChannelId(NotificationHelper.CHANNLE_ID1)

        startForeground(1818, nb.build())
    }


    //上传定位信息
    private fun uploadLocation() {


        if (!UserCenter.getInstance().isUserLogin) return


        val stb = StringBuffer()
        stb.append("定位是否开启=${LocationCenter.getInstance().locationIsStart()}\n")
        stb.append("当前时间=${TimeUtils.getCurrentTime()}\n")
        stb.append("速度=${currentPosition.speed}\n精度${currentPosition.accuracy}\n位置更新次数=$times\n")


        val type = when (currentPosition.locationType) {
            0 -> "定位失败"
            1 -> "GPS定位结果"
            2 -> "前次定位结果"
            4 -> "缓存定位结果"
            5 -> "Wifi定位结果"
            6 -> "基站定位结果"
            8 -> "离线定位结果"
            else -> "无效定位"
        }
        stb.append("定位类型=$type\n")


        val orderId = SharePreferenceCenter.getInstance().appShareP.orderId
        stb.append("orderId=$orderId\n")

        if (orderId == -1) {
            stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
            stb.append("订单为-1 上传失败\n")
            EventBusPro.post(stb.toString())
            return
        } else {
            startForegound()
        }

        stb.append("位置 当前=(${currentPosition.latitude},${currentPosition.longitude})\n")
        //todo 记得关掉
//        currentPosition.latitude = currentPosition.latitude + Math.random() * 1
//        stb.append("位置 后=(${currentPosition.latitude},${currentPosition.longitude})\n")
        val shareP = shareCenter.appShareP
        val uploadPosition = shareP.uploadPosition
        stb.append("位置 上次=(${uploadPosition.latitude},${uploadPosition.longitude})\n")

        val distance = AMapUtils.calculateLineDistance(currentPosition.toLatLng(), uploadPosition.toLatLng())
        stb.append("两点的距离：$distance\n")

        //判断是否是空位置
        if (currentPosition.isEmptyLocation) {
            stb.append("上传失败 是空位置\n")
            EventBusPro.post(stb.toString())
            return
        }
        //判断位置是否重复
        if (currentPosition.equals(uploadPosition)) {
            stb.append("上传失败：位置重复\n")
            EventBusPro.post(stb.toString())
            return
        }

        if (distance < 10) {
            stb.append("上传失败 小于10米")
            EventBusPro.post(stb.toString())
            return
        }

        if (currentPosition.accuracy > 100) {
            stb.append("上传失败：精度大于100")
            EventBusPro.post(stb.toString())
            return
        }

        if (currentPosition.speed < 0.1 && uploadPosition.isInit) {
            stb.append("上传失败：速度为${currentPosition.speed}\n")
            EventBusPro.post(stb.toString())
            return
        }

        //存储最新一份的 上传位置
        uploadPosition.latitude = currentPosition.latitude
        uploadPosition.longitude = currentPosition.longitude
        shareCenter.appShareP = shareP
        stb.append("位置不同，开始上传\n")
        mCase.uploadLocationV2(orderId,
                "${currentPosition.longitude}",
                "${currentPosition.latitude}",
                "${currentPosition.speed}",
                "${currentPosition.bearing}",
                "${currentPosition.time}").subscribe(object : OnRequestObserver<String>() {})
        times++
//        LocalLogUtils.printLogMsg(stb.toString())
        EventBusPro.post(stb.toString())
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        unInitScreenListener()
        unregisterLocationWakeReceiver()
        LocationCenter.getInstance().destroy()
        super.onDestroy()
    }

    //不断获取定位位置
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationChanged(amapLocation: AMapLocation) {
        //6为基站定位，位置较为不准，不发送过去校验
        if (amapLocation.locationType == 6||amapLocation.locationType==4) return
        val province = amapLocation.province//省信息
        val city = amapLocation.city//城市信息
        currentPosition.latitude = amapLocation.latitude
        currentPosition.longitude = amapLocation.longitude
        currentPosition.province = province
        currentPosition.city = city
        currentPosition.address = "我的位置"
        currentPosition.speed = amapLocation.speed
        currentPosition.bearing = amapLocation.bearing
        currentPosition.time = amapLocation.time
        currentPosition.accuracy = amapLocation.accuracy
        currentPosition.locationType = amapLocation.locationType
        //复制给AppData ->以后慢慢用这个服务的 currentPosition
        AppData.currentPosition = currentPosition

        uploadLocation()
    }


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
