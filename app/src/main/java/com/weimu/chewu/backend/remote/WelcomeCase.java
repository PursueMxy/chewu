package com.weimu.chewu.backend.remote;

import com.weimu.chewu.backend.bean.WelcomeB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.wallet.BankInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by huangjinfu on 18/5/23.
 */

public interface WelcomeCase {
    //获取欢迎页
    @GET("start_image")
    public Observable<NormalResponseB<WelcomeB>> getWelcome();

}
