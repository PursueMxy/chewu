package com.weimu.chewu.module.login.forget_pwd;

import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Created by huangjinfu on 18/5/2.
 */

public interface ForgetPwdContract {
    interface View extends BaseView {

        void resetSuccess();
    }


    interface Presenter extends BasePresenter {

        void doResetPwd(String phone,String code,String password);
    }
}
