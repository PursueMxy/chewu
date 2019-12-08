package com.weimu.chewu.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

/**
 * Created by 15 on 2018/3/9.
 */

public class NotificationHelper {


    public static final String CHANNLE_NAME1 = "chewu_channel";
    public static final String CHANNLE_ID1 = "7777";
    NotificationManager manager;

    private NotificationHelper(Context context) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    public static NotificationHelper with(Context context) {
        return new NotificationHelper(context);
    }

    /**
     * 创建通知渠道，适配8.0
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNLE_ID1, CHANNLE_NAME1, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(false); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            channel.enableVibration(false);
            channel.setSound(null, null);
            manager.createNotificationChannel(channel);
        }
    }

}
