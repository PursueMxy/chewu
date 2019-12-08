package com.weimu.chewu.module.login.check_register;

import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Created by huangjinfu on 18/5/2.
 */

public interface CheckRegisterContract {
    interface View extends BaseView {

        void checkSuccess(int status);
    }


    interface Presenter extends BasePresenter {

        void doCheck(String phone);
    }
}
