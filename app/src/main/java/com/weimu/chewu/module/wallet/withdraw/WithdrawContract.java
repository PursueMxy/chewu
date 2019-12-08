package com.weimu.chewu.module.wallet.withdraw;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Created by huangjinfu on 18/5/2.
 */

public interface WithdrawContract {

    interface View extends BaseView {

        void withdrawSuccess();
        void getUserInfoSuccess(UserB.CustomerBean customerBean);
    }


    interface Presenter extends BasePresenter {

        void doWithdraw(int card_id, int balance);
        void getUserInfo();
    }
}
