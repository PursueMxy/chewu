package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.base.NormalResponseB;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by huangjinfu on 18/5/1.
 */

public interface AddCardCase {
    //添加银行卡
    @FormUrlEncoded
    @POST("cards")
    Observable<NormalResponseB<String>> addBankCard(@Field("cardholder") String cardholder, @Field("card_no") String card_no, @Field("bank") String bank);


    //删除银行卡
    @FormUrlEncoded
    @PUT("cards/{id}")
    public Observable<NormalResponseB<String>> updateBankCard(@Path("id") String id,@Field("cardholder") String cardholder,@Field("card_no") String card_no,@Field("bank") String bank);

}
