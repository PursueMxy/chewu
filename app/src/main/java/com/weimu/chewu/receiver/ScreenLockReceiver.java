package com.weimu.chewu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Author:你需要一台永动机
 * Date:2018/9/6 15:55
 * Description:
 */
public class ScreenLockReceiver {
    private Context mContext;
    private ScreenBroadcastReceiver receiver;
    private ScreenStateListener mScreenStateListener;

    public ScreenLockReceiver(Context context) {
        mContext = context;
        receiver = new ScreenBroadcastReceiver();
    }

    public void register(ScreenStateListener screenStateListener) {
        if (screenStateListener != null) {
            mScreenStateListener = screenStateListener;
        }
        if (receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            mContext.registerReceiver(receiver, filter);
        }
    }

    public void unregister() {
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }


    private class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    if (mScreenStateListener != null) {
                        //Log.e("screen", "ScreenBroadcastReceiver --> ACTION_SCREEN_ON");
                        mScreenStateListener.onScreenOn();
                    }
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    if (mScreenStateListener != null) {
                        //Log.e("screen", "ScreenBroadcastReceiver --> ACTION_SCREEN_OFF");
                        mScreenStateListener.onScreenOff();

                    }
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    if (mScreenStateListener != null) {
                        //Log.e("screen", "ScreenBroadcastReceiver --> ACTION_USER_PRESENT");
                        mScreenStateListener.onUserPresent();
                    }
                }
            }
        }
    }

    public interface ScreenStateListener {
        void onScreenOn();

        void onScreenOff();

        void onUserPresent();
    }
}
