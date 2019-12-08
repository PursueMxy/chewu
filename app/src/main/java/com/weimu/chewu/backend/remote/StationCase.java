package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.StationListResultB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Author:你需要一台永动机
 * Date:2018/5/1 12:50
 * Description:监测站
 */
public interface StationCase {

    @GET("stations")
    Observable<NormalResponseB<StationListResultB>> getStations();
}
