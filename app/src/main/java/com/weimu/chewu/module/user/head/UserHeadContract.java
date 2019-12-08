package com.weimu.chewu.module.user.head;

import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Created by huangjinfu on 18/4/27.
 */

public interface UserHeadContract {

    interface View extends BaseView {

        void showSuccessResult(String userHead);
    }


    interface Presenter extends BasePresenter {

        void inputUserHead(String avatar);
    }
}
