package com.weimu.chewu.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.content.Context.POWER_SERVICE;

/**
 * Author:你需要一台永动机
 * Date:2018/9/1 00:59
 * Description:
 */
public class SystemUtils {
    /**
     * 检查是否可以显示通知
     *
     * @return
     */
    public static boolean isNotificationEnabled(Context mContext) {
        AppOpsManager mAppOps = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = mContext.getApplicationInfo();
        String pkg = mContext.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);
            return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //忽略电池优化
    public static void ignoreBatteryOptimization(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) mContext.getSystemService(POWER_SERVICE);

            boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(mContext.getPackageName());
            // 判断如果没有加入电池优化的白名单,则弹出加入电池优化的白名单的设置对话框
            if (!hasIgnored) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                mContext.startActivity(intent);
            }
        }
    }

    public static void enterWhiteListSetting(Context context) {
        try {
            context.startActivity(getSettingIntent());
        } catch (Exception e) {
            context.startActivity(new Intent(Settings.ACTION_SETTINGS));
        }
    }

    private static Intent getSettingIntent() {

        ComponentName componentName = null;

        String brand = android.os.Build.BRAND;

        switch (brand.toLowerCase()) {
            case "samsung":
                componentName = new ComponentName("com.samsung.android.sm_cn",
                        "com.samsung.android.sm.ui.ram.CstyleScoreBoardActivity");
                break;
            case "huawei":
            case "honor":
                componentName = new ComponentName("com.huawei.systemmanager",
                        "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity");
                break;
            case "xiaomi":
                componentName = new ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity");
                break;
            case "vivo":
                componentName = new ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity");
                break;
            case "oppo":
                componentName = new ComponentName("com.coloros.oppoguardelf",
                        "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity");
                break;
            case "360":
                componentName = new ComponentName("com.yulong.android.coolsafe",
                        "com.yulong.android.coolsafe.ui.activity.autorun.AutoRunListActivity");
                break;
            case "meizu":
                componentName = new ComponentName("com.meizu.safe",
                        "com.meizu.safe.permission.SmartBGActivity");
                break;
            case "oneplus":
                componentName = new ComponentName("com.oneplus.security",
                        "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity");
                break;
            case "Letv":
                componentName = new ComponentName("com.letv.android.letvsafe",
                        "com.letv.android.letvsafe.AutobootManageActivity");
                break;
            default:
                break;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (componentName != null) {
            intent.setComponent(componentName);
        } else {
            intent.setAction(Settings.ACTION_SETTINGS);
        }
        return intent;
    }

}
