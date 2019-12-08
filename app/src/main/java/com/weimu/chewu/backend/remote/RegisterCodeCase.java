package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.CheckRegisterInfo;
import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by huangjinfu on 18/5/16.
 */

public interface RegisterCodeCase {
    @FormUrlEncoded
    @POST("sms")
    Observable<NormalResponseB<String>> getCode(@Field("phone") String phone);


    @FormUrlEncoded
    @POST("checkout_code")
    Observable<NormalResponseB<String>> checkIdCode(@Field("phone") String phone,@Field("code") String code);

}
