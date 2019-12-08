package com.weimu.chewu.backend.local;

import android.content.Context;

import com.weimu.chewu.backend.bean.CityB;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:你需要一台永动机
 * Date:2018/4/25 00:59
 * Description:
 */
public interface CityListCase {

    public Observable<List<CityB>> getCityList(Context context);
}
