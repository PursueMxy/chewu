package com.weimu.chewu.backend.http.observer;

import com.orhanobut.logger.Logger;
import com.weimu.chewu.BuildConfig;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.http.core.ErrorHandler;
import com.weimu.chewu.origin.mvp.BaseView;
import com.weimu.chewu.widget.WMToast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class BaseObserver<T extends BaseB> implements Observer<T> {
    protected Disposable d;
    private BaseView mView;


    public BaseObserver() {
    }

    public BaseObserver(BaseView mView) {
        this.mView = mView;
    }

    @Override
    final public void onSubscribe(Disposable d) {
        this.d = d;
        if (mView != null) mView.showProgressBar();
        onStart(d);
    }


    @Override
    final public void onNext(T value) {
        if (mView != null) mView.hideProgressBar();
        //Logger.json(value.toJson());
        onSuccess(value);
        this.d.dispose();
    }

    @Override
    final public void onComplete() {

    }

    @Override
    final public void onError(Throwable e) {
        if (getView() != null) getView().hideProgressBar();
        String errorMessage = "";
        if (BuildConfig.DEBUG){
            errorMessage = new ErrorHandler(e).getErrorMessage();
            //errorMessage = e.toString();
        }else{
            errorMessage = new ErrorHandler(e).getErrorMessage();
        }
        Logger.e(e.getMessage());
        onFail(errorMessage);
        this.d.dispose();
    }


    //开始
    protected void onStart(Disposable d) {

    }

    //成功
    protected void onSuccess(T result) {

    }

    //错误
    protected void onFail(String e) {
        WMToast.show(e);
    }

    public BaseView getView() {
        return mView;
    }
}
