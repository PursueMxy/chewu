package com.weimu.chewu.module.wallet.bank.add;

import com.weimu.chewu.backend.bean.wallet.BankInfo;
import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Created by huangjinfu on 18/5/1.
 */

public interface AddCardContract {
    interface View extends BaseView {

        void addSuccess();
        void addFailed(String message);
    }


    interface Presenter extends BasePresenter {

        void addBankCard(String cardholder, String card_no, String bank);
        void updateBankCard(String id,String cardholder, String card_no, String bank);
    }
}
