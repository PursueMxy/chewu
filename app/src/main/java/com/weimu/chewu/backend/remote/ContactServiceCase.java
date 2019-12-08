package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.CustomerPhoneB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 00:08
 * Description:
 */
public interface ContactServiceCase {

    @GET("questions")
    Observable<PageB<ContactServiceB>> getQuestionsList(@Query("from") int page,
                                                        @Query("to") int pageSize);

    @GET("phone")
    Observable<NormalResponseB<CustomerPhoneB>> getServicePhone();

}
