package com.weimu.chewu.module.wallet.withdraw;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.TiXianCase;

/**
 * Created by huangjinfu on 18/5/2.
 */

public class WithdrawPresenterImpl implements WithdrawContract.Presenter {
    private CombineDisposable combineDisposable = new CombineDisposable();

    private WithdrawContract.View mView;
    private TiXianCase mCase;

    public WithdrawPresenterImpl(WithdrawContract.View mView) {
        this.mView = mView;
        mCase = new WithdrawCaseImpl();
    }


    @Override
    public void destroy() {
        combineDisposable.dispose();
    }


    @Override
    public void doWithdraw(int card_id, int balance) {
        mCase.doWithdraw(card_id, balance).subscribe(new OnRequestObserver<Object>() {
            @Override
            protected boolean OnSucceed(Object result) {
                mView.withdrawSuccess();
                return super.OnSucceed(result);
            }
        });
    }

    @Override
    public void getUserInfo() {
        mCase.getUserInfo().subscribe(new OnRequestObserver<UserB.CustomerBean>() {
            @Override
            protected boolean OnSucceed(UserB.CustomerBean result) {
                mView.getUserInfoSuccess(result);
                return super.OnSucceed(result);
            }
        });
    }
}
