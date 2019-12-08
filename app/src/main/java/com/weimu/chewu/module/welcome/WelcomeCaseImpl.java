package com.weimu.chewu.module.welcome;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.WelcomeB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.ContactServiceCase;
import com.weimu.chewu.backend.remote.WelcomeCase;

import io.reactivex.Observable;

/**
 * Created by huangjinfu on 18/5/23.
 */

public class WelcomeCaseImpl implements WelcomeCase {
    @Override
    public Observable<NormalResponseB<WelcomeB>> getWelcome() {
        return RetrofitClient.getDefault()
                .create(WelcomeCase.class)
                .getWelcome()
                .compose(RxSchedulers.<NormalResponseB<WelcomeB>>toMain());
    }
}
