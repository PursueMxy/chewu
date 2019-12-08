package com.weimu.chewu.module.login.forget_pwd;

import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.ForgetPwdCase;

/**
 * Created by huangjinfu on 18/5/2.
 */

public class ForgetPwdPresenterImpl implements ForgetPwdContract.Presenter {
    private CombineDisposable combineDisposable = new CombineDisposable();
    private ForgetPwdContract.View mView;
    private ForgetPwdCase mCase;

    public ForgetPwdPresenterImpl(ForgetPwdContract.View mView) {
        this.mView = mView;
        mCase = new ForgetPwdCaseImpl();

    }

    @Override
    public void destroy() {
        combineDisposable.dispose();
    }

    @Override
    public void doResetPwd(String phone, String code, String password) {
        mCase.resetPassword(phone, code, password)
                .subscribe(new OnRequestObserver<String>() {
                    @Override
                    protected boolean OnSucceed(String result) {
                        mView.resetSuccess();
                        return super.OnSucceed(result);
                    }

                    @Override
                    protected boolean onFailure(String message) {
                        return super.onFailure(message);
                    }
                });
    }
}
