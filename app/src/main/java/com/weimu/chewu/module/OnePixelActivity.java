package com.weimu.chewu.module;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Author:你需要一台永动机
 * Date:2018/9/6 16:00
 * Description:一像素的Activity
 */
public class OnePixelActivity extends Activity {

    private BroadcastReceiver endReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置1像素
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);

        //结束该页面的广播
        endReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("pixel", "OnePixelActivity finish");
                finish();
            }
        };
        registerReceiver(endReceiver, new IntentFilter("finishPixel"));
        //检查屏幕状态
        Log.e("pixel", "OnePixelActivity create");
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkScreen();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(endReceiver);
        super.onDestroy();
    }

    /**
     * 检查屏幕状态  isScreenOn为true  屏幕“亮”结束该Activity
     */
    private void checkScreen() {

        PowerManager pm = (PowerManager) OnePixelActivity.this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn) {
            Log.e("pixel", "OnePixelActivity finish");
            finish();
        }
    }
}
