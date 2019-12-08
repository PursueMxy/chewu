package com.weimu.chewu.backend.http.observer;


import com.weimu.chewu.Const;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.mvp.BaseView;
import com.weimu.chewu.utils.SharedDataTool;
import com.weimu.chewu.widget.WMToast;

import cn.jpush.android.api.JPushInterface;

import static com.weimu.universalib.OriginAppData.getContext;

/**
 * 用于处理有data的返回值
 */
public abstract class OnRequestObserver<T> extends BaseObserver<NormalResponseB<T>> {

    public OnRequestObserver() {

    }

    public OnRequestObserver(BaseView mView) {
        super(mView);
    }

    //默认不消费  成功
    protected boolean OnSucceed(T result) {
        return false;
    }

    //默认不消费  失败
    protected boolean onFailure(String message) {
        return false;
    }


    @Override
    final protected void onSuccess(NormalResponseB<T> value) {
        super.onSuccess(value);
        if (value.getCode() == Const.REQUEST_SUCCESS) {
            if (!OnSucceed(value.getData()))
                if ((getView() != null)) getView().showToast(value.getMessage());
        } else if (value.getCode() == Const.REQUEST_NEED_LOGIN) {
            UserCenter.getInstance().logOut();
            SharedDataTool.setBoolean(getContext(), "isAlias", false);
            JPushInterface.deleteAlias(getContext(), 1);

            WMToast.normal("此账号在别处登录");
        } else {
            if (!onFailure(value.getMessage())) super.onFail(value.getMessage());
        }
    }


    @Override
    final protected void onFail(String e) {
        if (!onFailure(e)) super.onFail(e);
    }


}
