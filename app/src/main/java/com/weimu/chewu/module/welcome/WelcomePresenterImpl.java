package com.weimu.chewu.module.welcome;

import com.weimu.chewu.backend.bean.WelcomeB;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.WelcomeCase;

/**
 * Created by huangjinfu on 18/5/23.
 */

public class WelcomePresenterImpl implements WelcomeContract.mPresenter {
    private CombineDisposable combineDisposable = new CombineDisposable();

    private WelcomeContract.mView mView;
    private WelcomeCase mCase;

    public WelcomePresenterImpl(WelcomeContract.mView mView) {
        this.mView = mView;
        mCase = new WelcomeCaseImpl();
    }

    @Override
    public void destroy() {
        combineDisposable.dispose();
    }

    @Override
    public void getImage() {
        mCase.getWelcome().subscribe(new OnRequestObserver<WelcomeB>() {
            @Override
            protected boolean OnSucceed(WelcomeB result) {

                mView.getImageSuccess(result.getUrl());
                return super.OnSucceed(result);
            }
        });
    }
}
