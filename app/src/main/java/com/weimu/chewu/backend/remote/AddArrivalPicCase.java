package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by huangjinfu on 18/9/2.
 */

public interface AddArrivalPicCase {
    //上传图片
    @FormUrlEncoded
    @POST("orders/reception")
    Observable<NormalResponseB<String>> uploadPic(@Field("order_id") int order_id,
                                                     @Field("car_images") String car_images);
}
