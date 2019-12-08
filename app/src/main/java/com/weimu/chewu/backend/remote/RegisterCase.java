package com.weimu.chewu.backend.remote;

import android.renderscript.BaseObj;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by huangjinfu on 18/4/22.
 */

public interface RegisterCase {

    @FormUrlEncoded
    @POST("register")
    Observable<NormalResponseB<BaseB>> registerReq(@Field("code") String code,
                                          @Field("phone") String phone,
                                          @Field("password") String password,
                                          @Field("name") String name,
                                          @Field("passport") String passport,
                                          @Field("passport_images") String passport_images,
                                          @Field("driver_license_images") String driver_license_images,
                                          @Field("driving_license_images") String driving_license_images);
}
