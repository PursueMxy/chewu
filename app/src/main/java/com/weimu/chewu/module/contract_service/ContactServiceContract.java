package com.weimu.chewu.module.contract_service;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.origin.list.mvp.UniversalListAction;
import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 00:16
 * Description:
 */
public interface ContactServiceContract {


    interface mView extends BaseView {
        void getPhone(String phone);
    }

    interface mPresenter extends BasePresenter {

        void setListAction(UniversalListAction<ContactServiceB> listAction);

        void getQuestionsList(int index, int pageSize);

        void getServicePhone();
    }
}
