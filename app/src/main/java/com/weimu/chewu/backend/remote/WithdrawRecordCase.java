package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.WithdrawRecordB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by huangjinfu on 18/5/11.
 */

public interface WithdrawRecordCase {
    @GET("balance_records")
    Observable<NormalResponseB<PageB<WithdrawRecordB>>> getWithdrawRecordList(@Query("from") int page,
                                                                         @Query("to") int pageSize);

}
