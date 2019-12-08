package com.weimu.chewu.backend.http.observer;

import com.orhanobut.logger.Logger;
import com.weimu.chewu.Const;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.WMToast;

/**
 * 用于处理列表数据
 */
public abstract class OnRequestListListener<T> extends BaseObserver<PageB<T>> {


    public OnRequestListListener(BaseActivity baseActivity) {
        super(baseActivity);
    }

    public OnRequestListListener() {

    }


    //默认不消费  成功
    protected boolean OnSucceed(PageB<T> value) {
        return false;
    }

    //默认不消费  失败
    protected boolean onFailure(String message) {
        return false;
    }


    @Override
    final protected void onSuccess(PageB<T> value) {
        if (value.getCode() == Const.REQUEST_SUCCESS) {
            if (!OnSucceed(value))
                if ((getView() != null)) getView().showToast(value.getMessage());
        } else if (value.getCode() == Const.REQUEST_NEED_LOGIN) {
            UserCenter.getInstance().logOut();

        } else {
            if (!onFailure(value.getMessage())) onFail(value.getMessage());
        }
    }

    @Override
    final protected void onFail(String e) {
        super.onFail(e);
    }
}
