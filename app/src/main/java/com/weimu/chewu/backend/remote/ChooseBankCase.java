package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.OrderListResultB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.bean.wallet.BankInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by huangjinfu on 18/5/1.
 */

public interface ChooseBankCase {
    //获取银行卡列表
    @GET("cards")
    public Observable<NormalResponseB<List<BankInfo>>> getBankList();

    //删除银行卡
    @DELETE("cards/{id}")
    public Observable<NormalResponseB<String>> deleteBankCard(@Path("id") String id);
}
