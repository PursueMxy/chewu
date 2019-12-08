package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.OrderListResultB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author:你需要一台永动机
 * Date:2018/4/29 16:05
 * Description:
 */
public interface OrderListCase {

    //获取订单列表
    @GET("customer/orders")
    public Observable<NormalResponseB<OrderListResultB>> getOrderList(@Query("status") String status, @Query("page") int page);


}
