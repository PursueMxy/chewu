package com.weimu.universalib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author:你需要一台永动机
 * Date:2018/3/23 16:23
 * Description:
 */

public class TimeUtils {

    //todo 格式的显示要多调试
    //格式化时间 秒->倒计时
    public static String formatSecond2CountDownTime(int seconds) {
        if (seconds < 0 || seconds > 24 * 60 * 60) {
            return "??h??m";
        }
        if (seconds < 60)
            return "<1m";
        //long day = seconds / (24 * 60 * 60);
        //seconds = seconds % (24 * 60 * 60);
        long hour = seconds / (60 * 60);
        seconds = seconds % (60 * 60);
        long minute = seconds / 60;
        long second = seconds % 60;
        StringBuilder sb = new StringBuilder();
        //if (day > 0) sb.append(day + "天");
        sb.append(hour + "h");
        sb.append(minute + "m");
        //sb.append(second + "S");
        return sb.toString();
    }

    //格式化时间 秒->进度 1000
    public static int formatSecond2Progress(int seconds) {
        if (seconds < 0 || seconds > 24 * 60 * 60) {
            return 0;
        }
        return (int) ((1 - (seconds / (24 * 60 * 60f))) * 1000);
    }


    public static String getCurrentTime() {
        Date date = Calendar.getInstance().getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

    }
}
