package com.weimu.chewu.module.order;

import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Author:你需要一台永动机
 * Date:2018/5/19 14:53
 * Description:
 */
public interface OrderListContract {

    interface View extends BaseView {

    }


    interface Presenter extends BasePresenter {
        public void getOrderList(String status, int page);
    }
}
