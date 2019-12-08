package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.Position4PathB;
import com.weimu.chewu.backend.bean.OrderDetailB;
import com.weimu.chewu.backend.bean.OrderListResultB;
import com.weimu.chewu.backend.bean.PositionB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author:你需要一台永动机
 * Date:2018/4/29 20:13
 * Description:
 */
public interface OrderDetailCase {


    //获取订单详情
    @GET("orders/{id}")
    Observable<NormalResponseB<OrderDetailB>> getOrderList(@Path("id") String id);


    //添加备注
    //@Headers("Content-Type:application/x-www-form-urlencoded; charset=UTF-8")
    @FormUrlEncoded
    @POST("orders/backup")
    Observable<NormalResponseB<String>> addRemark(@Field("order_id") int order_id, @Field("content") String content);

    //上传监测站
    @FormUrlEncoded
    @POST("orders/station")
    Observable<NormalResponseB<String>> receiveGuest(@Field("order_id") int order_id,
                                                     @Field("station_id") int station_id);


    //上传检测结果
    @FormUrlEncoded
    @POST("orders/result")
    Observable<NormalResponseB<String>> updateCheckResult(@Field("order_id") int order_id,
                                                          @Field("checkout_result") String checkout_result,
                                                          @Field("failed_reason_img") String failed_reason_img);

    //结束订单
    @FormUrlEncoded
    @POST("orders/finished")
    Observable<NormalResponseB<String>> finishOrder(@Field("order_id") int order_id,
                                                    @Field("electron_sign_img") String electron_sign_img);




    //获取车辆路径
    @GET("orders/{id}/location")
    Observable<NormalResponseB<List<Position4PathB>>> getLocation(@Path("id") int id);
}
