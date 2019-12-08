package com.weimu.chewu.module.register;

import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.RegisterCase;

import io.reactivex.disposables.Disposable;

/**
 * Created by huangjinfu on 18/4/22.
 */

public class RegisterPersenterImpl implements RegisterContract.Presenter {
    private CombineDisposable combineDisposable = new CombineDisposable();

    private RegisterContract.View mView;
    private RegisterCase mCase;


    public RegisterPersenterImpl(RegisterContract.View mView) {
        this.mView = mView;
        mCase = new RegisterCaseImpl();
    }


    @Override
    public void register(String code, String phone, String password, String name, String passport, String passport_images, String driver_license_images, String driving_license_images) {
        boolean disposable = combineDisposable.isDisposable("register");
        if (!disposable) return;
        //mView传入 有圆形进度条  不传入 不会显示圆形进度条
        mCase.registerReq(code, phone, password, name, passport, passport_images, driver_license_images, driving_license_images)

                .subscribe(new OnRequestObserver<BaseB>() {

                    @Override
                    protected void onStart(Disposable d) {
                        combineDisposable.addDisposable("register", d);
                    }

                    @Override
                    protected boolean OnSucceed(BaseB result) {
                        mView.showSuccessResult();
                        return super.OnSucceed(result);
                    }


                });
    }

    @Override
    public void destroy() {
        combineDisposable.dispose();
    }
}
