package com.weimu.chewu.module.city;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.weimu.chewu.backend.bean.CityB;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.local.CityListCase;
import com.weimu.universalib.utils.AssetsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Author:你需要一台永动机
 * Date:2018/4/25 01:00
 * Description:
 */
public class CityListCaseImpl implements CityListCase {


    @Override
    public Observable<List<CityB>> getCityList(final Context context) {
        return new Observable<List<CityB>>() {

            @Override
            protected void subscribeActual(Observer<? super List<CityB>> observer) {
                List<CityB> cities = new ArrayList<>();
                String json = AssetsUtils.getJson(context, "cities.json");
                JsonObject rootObj = new JsonParser().parse(json).getAsJsonObject();
                for (String item : rootObj.keySet()) {
                    //for循环获取 城市
                    String city = rootObj.get(item).getAsString();
                    cities.add(new CityB(item, city));
                }
                Collections.sort(cities);
                observer.onNext(cities);
            }
        }.compose(RxSchedulers.<List<CityB>>toMain());
    }
}
