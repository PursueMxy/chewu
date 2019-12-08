package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.CheckRegisterInfo;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by huangjinfu on 18/5/2.
 */

public interface ForgetPwdCase {
    @FormUrlEncoded
    @PUT("reset_password")
    Observable<NormalResponseB<String>> resetPassword(@Field("phone") String phone, @Field("code") String code, @Field("new_password") String new_password);

}
