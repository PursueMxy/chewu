package com.weimu.chewu.module.login.check_register;

import com.weimu.chewu.backend.bean.CheckRegisterInfo;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.CheckRegisterCase;

/**
 * Created by huangjinfu on 18/5/2.
 */

public class CheckRegisterPresenterImpl implements CheckRegisterContract.Presenter {
    private CombineDisposable combineDisposable = new CombineDisposable();
    private CheckRegisterContract.View mView;
    private CheckRegisterCase mCase;

    public CheckRegisterPresenterImpl(CheckRegisterContract.View mView) {
        this.mView = mView;
        mCase = new CheckRegisterCaseImpl();

    }

    @Override
    public void destroy() {
        combineDisposable.dispose();
    }

    @Override
    public void doCheck(String phone) {
        mCase.checkRegister(phone)
                .subscribe(new OnRequestObserver<CheckRegisterInfo>() {
                    @Override
                    protected boolean OnSucceed(CheckRegisterInfo result) {
                        mView.checkSuccess(result.getStatus());
                        return super.OnSucceed(result);
                    }

                    @Override
                    protected boolean onFailure(String message) {
                        return super.onFailure(message);
                    }
                });
    }
}
