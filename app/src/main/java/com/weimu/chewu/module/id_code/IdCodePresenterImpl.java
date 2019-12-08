package com.weimu.chewu.module.id_code;

import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.RegisterCodeCase;

/**
 * Created by huangjinfu on 18/5/16.
 */

public class IdCodePresenterImpl implements IdCodeContract.mPresenter {
    private CombineDisposable combineDisposable = new CombineDisposable();


    private IdCodeContract.mView mView;
    private RegisterCodeCase mCase;

    public IdCodePresenterImpl(IdCodeContract.mView mView) {
        this.mView = mView;
        mCase = new IdCodeCaseImpl();
    }


    @Override
    public void destroy() {
        combineDisposable.dispose();
    }

    @Override
    public void getIdCode(String phone) {
        mCase.getCode(phone).subscribe(new OnRequestObserver<String>() {
            @Override
            protected boolean OnSucceed(String result) {
                mView.getCodeSuccess();
                return super.OnSucceed(result);
            }

            @Override
            protected boolean onFailure(String message) {
                return super.onFailure("发送多次，请求失败");
            }
        });

    }

    @Override
    public void checkIdCode(String phone, String code) {
        mCase.checkIdCode(phone, code).subscribe(new OnRequestObserver<String>() {
            @Override
            protected boolean OnSucceed(String result) {
                mView.checkIdCodeSuccess();
                return super.OnSucceed(result);
            }
        });
    }
}
