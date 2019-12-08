package com.weimu.chewu.module.message;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.MessageCenterB;
import com.weimu.chewu.origin.list.mvp.UniversalListAction;
import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Created by huangjinfu on 18/5/14.
 */

public interface MessageCenterContract {
    interface mView extends BaseView {

    }

    interface mPresenter extends BasePresenter {

        void setListAction(UniversalListAction<MessageCenterB> listAction);

        void getMessageList(int index, int pageSize);
    }
}
