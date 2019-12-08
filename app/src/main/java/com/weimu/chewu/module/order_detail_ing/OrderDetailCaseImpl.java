package com.weimu.chewu.module.order_detail_ing;

import com.weimu.chewu.backend.Position4PathB;
import com.weimu.chewu.backend.bean.OrderDetailB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.OrderDetailCase;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:你需要一台永动机
 * Date:2018/4/29 20:16
 * Description:
 */
public class OrderDetailCaseImpl implements OrderDetailCase {

    @Override
    public Observable<NormalResponseB<OrderDetailB>> getOrderList(String id) {
        return RetrofitClient.getDefault()
                .create(OrderDetailCase.class)
                .getOrderList(id)
                .compose(RxSchedulers.<NormalResponseB<OrderDetailB>>toMain());
    }

    @Override
    public Observable<NormalResponseB<String>> addRemark(int order_id, String content) {
        return RetrofitClient.getDefault()
                .create(OrderDetailCase.class)
                .addRemark(order_id, content)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }

    @Override
    public Observable<NormalResponseB<String>> receiveGuest(int order_id,  int station_id) {
        return RetrofitClient.getDefault()
                .create(OrderDetailCase.class)
                .receiveGuest(order_id, station_id)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }

    @Override
    public Observable<NormalResponseB<String>> updateCheckResult(int order_id, String checkout_result, String failed_reason_img) {
        return RetrofitClient.getDefault()
                .create(OrderDetailCase.class)
                .updateCheckResult(order_id, checkout_result, failed_reason_img)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }

    @Override
    public Observable<NormalResponseB<String>> finishOrder(int order_id, String electron_sign_img) {
        return RetrofitClient.getDefault()
                .create(OrderDetailCase.class)
                .finishOrder(order_id, electron_sign_img)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }

    @Override
    public Observable<NormalResponseB<List<Position4PathB>>> getLocation(int id) {
        return RetrofitClient.getDefault()
                .create(OrderDetailCase.class)
                .getLocation(id)
                .compose(RxSchedulers.<NormalResponseB<List<Position4PathB>>>toMain());
    }

}
