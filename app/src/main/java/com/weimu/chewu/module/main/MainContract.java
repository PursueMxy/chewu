package com.weimu.chewu.module.main;

import com.amap.api.maps.model.Marker;
import com.weimu.chewu.backend.bean.OrderInMapB;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

import java.util.List;

/**
 * Author:你需要一台永动机
 * Date:2018/4/1 23:29
 * Description:
 */
public interface MainContract {
    interface MainView extends BaseView {


        void loadOrderList(List<OrderInMapB> result);

        void showContractDialog(Marker marker, OrderInMapB orderInMapB);

        void logoutSuccess();

        void receiveOrderFail();

        void setAllReadSuccess();

        void showMessageRedDot(boolean isShow);

    }

    interface MainPresenter extends BasePresenter {

        void getOrderInMap(String province, String city, String county);

        void getUserInfo(String token);

        void orderReceiving(Marker marker, OrderInMapB orderInMapB);

        void confirmOrder(int order_id);

        void cancelOrder(int order_id);

        void doLogout();


        void getCurrentOrderID();

        void setAllRead();

        void getMessageList();
    }
}
