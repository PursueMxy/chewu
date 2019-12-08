package com.weimu.chewu.module.id_code;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.origin.list.mvp.UniversalListAction;
import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Created by huangjinfu on 18/5/16.
 */

public interface IdCodeContract {
    interface mView extends BaseView {
        void getCodeSuccess();
        void checkIdCodeSuccess();
    }

    interface mPresenter extends BasePresenter {

        void getIdCode(String phone);

        void checkIdCode(String phone, String code);
    }

}
