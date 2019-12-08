package com.weimu.chewu.backend.http.observer;


import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.BaseResponseB;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * 用于处理无data的返回值
 */
public abstract class OnSingleRequestObserver<T extends BaseB> extends BaseObserver<T> {

    public OnSingleRequestObserver() {
    }

    public OnSingleRequestObserver(BaseView mView) {
        super(mView);
    }

    @Override
    protected void onSuccess(T result) {
        super.onSuccess(result);
        //if (getView() != null) WMToast.showSuccess(result.getMessage());
    }
}
