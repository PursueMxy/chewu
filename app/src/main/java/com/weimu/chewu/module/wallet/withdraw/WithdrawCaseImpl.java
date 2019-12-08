package com.weimu.chewu.module.wallet.withdraw;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.LoginCase;
import com.weimu.chewu.backend.remote.TiXianCase;

import io.reactivex.Observable;

/**
 * Created by huangjinfu on 18/5/2.
 */

public class WithdrawCaseImpl implements TiXianCase {
    @Override
    public Observable<NormalResponseB<Object>> doWithdraw(int card_id, int balance) {
        return RetrofitClient.getDefault()
                .create(TiXianCase.class)
                .doWithdraw(card_id, balance)
                .compose(RxSchedulers.<NormalResponseB<Object>>toMain());

    }

    @Override
    public Observable<NormalResponseB<UserB.CustomerBean>> getUserInfo() {
        return RetrofitClient.getDefault()
                .create(TiXianCase.class)
                .getUserInfo()
                .compose(RxSchedulers.<NormalResponseB<UserB.CustomerBean>>toMain());
    }
}
