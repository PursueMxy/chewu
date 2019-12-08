package com.weimu.chewu.module.loginx;

import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Author:你需要一台永动机
 * Date:2018/4/21 11:37
 * Description:
 */
public interface LoginContract {

    interface View extends BaseView {

        void showSuccessResult();
    }


    interface Presenter extends BasePresenter {

        void login(String phone, String password);
    }
}
