package com.weimu.universalib.utils;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:你需要一台永动机
 * Date:2018/2/3 15:51
 * Description:
 */

public class EventBusPro {

    public static void post(Object object) {
        if (object instanceof String) {
            Log.d("uplocation", "┌────────────────────────────────────────────────────");
            Log.d("uplocation", object.toString());
            Log.d("uplocation", "└────────────────────────────────────────────────────");
        }
        EventBus.getDefault().post(object);
    }

    public static void postDelay(final Object object, long time) {
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        EventBus.getDefault().post(object);
                    }
                });


    }
}
