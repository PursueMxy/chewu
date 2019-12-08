package com.weimu.chewu.module.wallet.withdraw_record;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.WithdrawRecordB;
import com.weimu.chewu.origin.list.mvp.UniversalListAction;
import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Created by huangjinfu on 18/5/11.
 */

public interface WithdrawRecordContract {
    interface mView extends BaseView {

    }

    interface mPresenter extends BasePresenter {

        void setListAction(UniversalListAction<WithdrawRecordB> listAction);

        void getRecordList(int index, int pageSize);
    }
}
