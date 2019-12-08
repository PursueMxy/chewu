package com.weimu.chewu.module.user.head;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.LoginCase;
import com.weimu.chewu.backend.remote.UserHeadCase;

import io.reactivex.Observable;

/**
 * Created by huangjinfu on 18/4/27.
 */

public class UserHeadCaseImpl implements UserHeadCase{

    @Override
    public Observable<NormalResponseB<String>> inputUserHead(String adatar) {
        return RetrofitClient.getDefault()
                .create(UserHeadCase.class)
                .inputUserHead(adatar)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }


}
