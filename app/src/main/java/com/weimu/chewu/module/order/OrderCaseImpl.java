package com.weimu.chewu.module.order;

import com.weimu.chewu.backend.bean.OrderListResultB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.OrderListCase;

import io.reactivex.Observable;

/**
 * Author:你需要一台永动机
 * Date:2018/4/29 16:16
 * Description:订单
 */
public class OrderCaseImpl implements OrderListCase {
    @Override
    public Observable<NormalResponseB<OrderListResultB>> getOrderList(String status, int page) {
        return RetrofitClient.getDefault()
                .create(OrderListCase.class)
                .getOrderList(status, page)
                .compose(RxSchedulers.<NormalResponseB<OrderListResultB>>toMain());
    }
}
