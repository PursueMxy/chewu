package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by huangjinfu on 18/4/27.
 */

public interface UserHeadCase {
    @FormUrlEncoded
    @PUT("customer")
    Observable<NormalResponseB<String>> inputUserHead(@Field("avatar") String adatar);

}
