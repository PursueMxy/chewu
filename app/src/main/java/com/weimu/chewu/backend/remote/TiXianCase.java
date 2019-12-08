package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by huangjinfu on 18/5/1.
 */

public interface TiXianCase {

    @FormUrlEncoded
    @POST("withdraw")
    Observable<NormalResponseB<Object>> doWithdraw(@Field("card_id") int card_id, @Field("balance") int balance);


    /**
     * 获取个人信息
     *
     * @return
     */
    @GET("customer")
    Observable<NormalResponseB<UserB.CustomerBean>> getUserInfo();


}
