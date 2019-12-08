package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.MessageCenterB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by huangjinfu on 18/5/13.
 */

public interface MessageCenterCase {
    @GET("notifications")
    Observable<PageB<MessageCenterB>> getMessageList(@Query("from") int page,
                                                     @Query("to") int pageSize);

}
