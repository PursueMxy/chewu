package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.CheckRegisterInfo;
import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by huangjinfu on 18/5/2.
 */

public interface CheckRegisterCase {
    @FormUrlEncoded
    @POST("checkout_register")
    Observable<NormalResponseB<CheckRegisterInfo>> checkRegister(@Field("phone") String phone);

}
