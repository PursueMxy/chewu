package com.weimu.chewu.module.station;

import com.weimu.chewu.backend.bean.StationListResultB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.StationCase;

import io.reactivex.Observable;

/**
 * Author:你需要一台永动机
 * Date:2018/5/1 12:53
 * Description:
 */
public class StationCaseImpl implements StationCase {

    @Override
    public Observable<NormalResponseB<StationListResultB>> getStations() {
        return RetrofitClient.getDefault()
                .create(StationCase.class)
                .getStations().compose(RxSchedulers.<NormalResponseB<StationListResultB>>toMain());
    }
}
