package com.weimu.chewu.backend.remote;

import com.weimu.chewu.AppData;
import com.weimu.chewu.backend.bean.OrderInMapB;
import com.weimu.chewu.backend.bean.OrderListResultB;
import com.weimu.chewu.backend.bean.QiNiuTokenB;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.origin.center.UserCenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.weimu.chewu.AppData.*;

/**
 * Author:你需要一台永动机
 * Date:2018/4/18 22:02
 * Description:
 */
public interface MainCase {

    //获取七牛token
    @GET("qiniu_token")
    Observable<QiNiuTokenB> getQiNiuToken();


    /**
     * 获取接单区域订单列表
     */
    @GET("orders")
    Observable<NormalResponseB<List<OrderInMapB>>> getOrderInMap(
            @Query("province") String province,
            @Query("city") String city,
            @Query("county") String county);

    /**
     * 获取个人信息
     *
     * @return
     */
    @GET("customer")
    Observable<NormalResponseB<UserB.CustomerBean>> getUserInfo(@Header("Authorization") String token);


    /**
     * 接单
     */
    @FormUrlEncoded
    @POST("orders/accepting")
    Observable<NormalResponseB<BaseB>> orderReceiving(@Field("order_id") int order_id);


    /**
     * 确认接单
     */
    @FormUrlEncoded
    @POST("orders/accepted")
    Observable<NormalResponseB<BaseB>> confirmOrder(@Field("order_id") int order_id);


    /**
     * 取消接单
     */
    @FormUrlEncoded
    @POST("orders/unaccepted")
    Observable<NormalResponseB<BaseB>> cancelOrder(@Field("order_id") int order_id);

    //退出登录
    @POST("logout")
    Observable<NormalResponseB<String>> logout();

    //上传车辆路径
    @FormUrlEncoded
    @POST("orders/{id}/location")
    Observable<NormalResponseB<String>> uploadLocation(@Path("id") int id, @Field("location") String location);

    //上传车辆路径
    @FormUrlEncoded
    @POST("orders/{id}/correct_location")
    Observable<NormalResponseB<String>> uploadLocationV2(@Path("id") int id,
                                                         @Field("lng") String lng,
                                                         @Field("lat") String lat,
                                                         @Field("speed") String speed,
                                                         @Field("bearing") String bearing,
                                                         @Field("time") String time);

    //获取订单列表  获取最新的orderID
    @GET("customer/orders")
    public Observable<NormalResponseB<OrderListResultB>> getOrderList(@Query("status") String status, @Query("page") int page);

    //设置未读
    @POST("notifications/read")
    Observable<NormalResponseB<String>> setAllRead();



}
