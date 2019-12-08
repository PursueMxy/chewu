package com.weimu.chewu.module.welcome;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.origin.list.mvp.UniversalListAction;
import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Created by huangjinfu on 18/5/23.
 */

public interface WelcomeContract {
    interface mView extends BaseView {
        void getImageSuccess(String phone);
    }

    interface mPresenter extends BasePresenter {

        void getImage();
    }
}
