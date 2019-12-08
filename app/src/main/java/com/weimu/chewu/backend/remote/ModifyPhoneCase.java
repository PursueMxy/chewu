package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

/**
 * Created by huangjinfu on 18/4/30.
 */

public interface ModifyPhoneCase {
    @FormUrlEncoded
    @PUT("reset_phone")
    Observable<NormalResponseB<String>> resetPhone(@Field("phone") String phone,@Field("code") String code);

}
