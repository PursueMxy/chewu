package com.weimu.chewu.backend.remote;


import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Author:你需要一台永动机
 * Date:2018/3/8 15:12
 * Description:
 */

public interface LoginCase {

    /**
     * 登录操作
     */
    @FormUrlEncoded
    @POST("login")
    Observable<NormalResponseB<UserB>> loginReq(@Field("phone") String phone, @Field("password") String password);
}
