package com.weimu.chewu.module.main;

import android.util.Log;

import com.weimu.chewu.backend.bean.OrderInMapB;
import com.weimu.chewu.backend.bean.OrderListResultB;
import com.weimu.chewu.backend.bean.QiNiuTokenB;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.MainCase;
import com.weimu.chewu.backend.remote.OrderDetailCase;
import com.weimu.chewu.backend.remote.OrderListCase;
import com.weimu.chewu.origin.center.UserCenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Author:你需要一台永动机
 * Date:2018/4/23 23:50
 * Description:
 */
public class MainCaseImpl implements MainCase {


    @Override
    public Observable<QiNiuTokenB> getQiNiuToken() {
        return RetrofitClient.getDefault()
                .create(MainCase.class)
                .getQiNiuToken()
                .compose(RxSchedulers.<QiNiuTokenB>toMain());
    }

    @Override
    public Observable<NormalResponseB<List<OrderInMapB>>> getOrderInMap(String province, String city, String county) {
        return RetrofitClient.getDefault()
                .create(MainCase.class)
                .getOrderInMap(null, null, null)
                .compose(RxSchedulers.<NormalResponseB<List<OrderInMapB>>>toMain());
    }


    @Override
    public Observable<NormalResponseB<UserB.CustomerBean>> getUserInfo(String token) {
        return RetrofitClient.getDefault()
                .create(MainCase.class)
                .getUserInfo(token)
                .compose(RxSchedulers.<NormalResponseB<UserB.CustomerBean>>toMain());
    }

    @Override
    public Observable<NormalResponseB<BaseB>> orderReceiving(int order_id) {
        return RetrofitClient.getDefault()
                .create(MainCase.class)
                .orderReceiving(order_id)
                .compose(RxSchedulers.<NormalResponseB<BaseB>>toMain());
    }

    @Override
    public Observable<NormalResponseB<BaseB>> confirmOrder(int order_id) {
        return RetrofitClient.getDefault()
                .create(MainCase.class)
                .confirmOrder(order_id)
                .compose(RxSchedulers.<NormalResponseB<BaseB>>toMain());
    }

    @Override
    public Observable<NormalResponseB<BaseB>> cancelOrder(int order_id) {
        return RetrofitClient.getDefault()
                .create(MainCase.class)
                .cancelOrder(order_id)
                .compose(RxSchedulers.<NormalResponseB<BaseB>>toMain());
    }

    @Override
    public Observable<NormalResponseB<String>> logout() {
        return RetrofitClient.getDefault()
                .create(MainCase.class)
                .logout()
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }


    @Override
    public Observable<NormalResponseB<String>> uploadLocation(int id, String locations) {
        return RetrofitClient.getDefault()
                .create(MainCase.class)
                .uploadLocation(id, locations)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }

    @Override
    public Observable<NormalResponseB<String>> uploadLocationV2(int id, String lng, String lat, String speed, String bearing, String time) {
        return RetrofitClient.getDefault()
                .create(MainCase.class)
                .uploadLocationV2(id, lng, lat, speed, bearing, time)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }


    @Override
    public Observable<NormalResponseB<OrderListResultB>> getOrderList(String status, int page) {
        return RetrofitClient.getDefault()
                .create(OrderListCase.class)
                .getOrderList(status, page)
                .compose(RxSchedulers.<NormalResponseB<OrderListResultB>>toMain());
    }

    @Override
    public Observable<NormalResponseB<String>> setAllRead() {
        return RetrofitClient.getDefault()
                .create(MainCase.class)
                .setAllRead()
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }

}
