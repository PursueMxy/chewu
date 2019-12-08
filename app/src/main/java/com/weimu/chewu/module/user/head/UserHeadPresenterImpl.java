package com.weimu.chewu.module.user.head;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.remote.LoginCase;
import com.weimu.chewu.backend.remote.UserHeadCase;
import com.weimu.chewu.module.loginx.LoginCaseImpl;
import com.weimu.chewu.module.loginx.LoginContract;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by huangjinfu on 18/4/27.
 */

public class UserHeadPresenterImpl implements UserHeadContract.Presenter {
    private CombineDisposable combineDisposable = new CombineDisposable();

    private UserHeadContract.View mView;
    private UserHeadCase mCase;

    public UserHeadPresenterImpl(UserHeadContract.View mView) {
        this.mView = mView;
        mCase = new UserHeadCaseImpl();
    }

    @Override
    public void destroy() {
        combineDisposable.dispose();
    }

    @Override
    public void inputUserHead(final String avatar) {
        mCase.inputUserHead(avatar).subscribe(new Observer<NormalResponseB<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(NormalResponseB<String> stringNormalResponseB) {
                mView.showSuccessResult(avatar);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
