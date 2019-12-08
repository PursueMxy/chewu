package com.weimu.chewu.module.main;

import android.util.Log;

import com.amap.api.maps.model.Marker;
import com.weimu.chewu.backend.bean.MessageCenterB;
import com.weimu.chewu.backend.bean.OrderInMapB;
import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.bean.OrderListResultB;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestListListener;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.MainCase;
import com.weimu.chewu.backend.remote.MessageCenterCase;
import com.weimu.chewu.module.message.MessageCenterCaseImpl;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.type.OrderStatus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Author:你需要一台永动机
 * Date:2018/4/1 23:30
 * Description:
 */
public class MainPresenterImpl implements MainContract.MainPresenter {
    private CombineDisposable combineDisposable = new CombineDisposable();
    private MainContract.MainView mView;
    private MainCase mCase;
    private MessageCenterCase mCaseV2;

    public MainPresenterImpl(MainContract.MainView mView) {
        this.mView = mView;
        mCase = new MainCaseImpl();
        mCaseV2 = new MessageCenterCaseImpl();
    }

    @Override
    public void destroy() {
        combineDisposable.dispose();
    }


    @Override
    public void getOrderInMap(String province, String city, String county) {
        if (!UserCenter.getInstance().isUserLogin()) return;
        if (!combineDisposable.isDisposable("getOrderInMap"))
            province = "福建省";
        city = "厦门市";
        county = "思明区";
        mCase.getOrderInMap(province, city, county).subscribe(new OnRequestObserver<List<OrderInMapB>>() {
            @Override
            protected void onStart(Disposable d) {
                super.onStart(d);
                combineDisposable.addDisposable("getOrderInMap", d);
            }

            @Override
            protected boolean OnSucceed(List<OrderInMapB> result) {
                mView.loadOrderList(result);
                return super.OnSucceed(result);
            }

            @Override
            protected boolean onFailure(String message) {
                return super.onFailure(message);
            }
        });
    }

    @Override
    public void getUserInfo(String token) {
        if (!combineDisposable.isDisposable("getUserInfo"))
            mCase.getUserInfo(token).subscribe(new OnRequestObserver<UserB.CustomerBean>() {

                @Override
                protected void onStart(Disposable d) {
                    super.onStart(d);
                    combineDisposable.addDisposable("getUserInfo", d);
                }
            });
    }

    //接单
    @Override
    public void orderReceiving(final Marker marker, final OrderInMapB orderInMapB) {
        if (!combineDisposable.isDisposable("orderReceiving"))
            mCase.orderReceiving(orderInMapB.getId());
            mCase.orderReceiving(orderInMapB.getId()).subscribe(new OnRequestObserver<BaseB>() {
                @Override
                protected void onStart(Disposable d) {
                    super.onStart(d);
                    combineDisposable.addDisposable("orderReceiving", d);
                }

                @Override
                protected boolean OnSucceed(BaseB result) {
                    mView.showContractDialog(marker, orderInMapB);
                    return super.OnSucceed(result);
                }

                @Override
                protected boolean onFailure(String message) {
                    mView.receiveOrderFail();
                    return super.onFailure(message);
                }
            });
    }

    //确定订单
    @Override
    public void confirmOrder(final int order_id) {
        if (!combineDisposable.isDisposable("confirmOrder"))
            mCase.confirmOrder(order_id);
            mCase.confirmOrder(order_id).subscribe(new OnRequestObserver<BaseB>() {
                @Override
                protected void onStart(Disposable d) {
                    combineDisposable.addDisposable("confirmOrder", d);
                }

                @Override
                protected boolean OnSucceed(BaseB result) {
                    getOrderInMap(null, null, null);
//                SharePreferenceCenter center = SharePreferenceCenter.getInstance();
//                AppSharePreB appShareP = center.getAppShareP();
//                appShareP.setOrderId(order_id);
//                appShareP.setUploadPosition(null);
//                center.setAppShareP(appShareP);
                    return super.OnSucceed(result);
                }

                @Override
                protected boolean onFailure(String message) {
                    Log.e("确认接单错误",message);
                    return super.onFailure(message);
                }
            });
    }

    //取消订单
    @Override
    public void cancelOrder(int order_id) {
        if (!combineDisposable.isDisposable("cancelOrder"))
            mCase.cancelOrder(order_id).subscribe(new OnRequestObserver<BaseB>() {
                @Override
                protected void onStart(Disposable d) {
                    combineDisposable.addDisposable("cancelOrder", d);
                }

                @Override
                protected boolean OnSucceed(BaseB result) {
                    getOrderInMap(null, null, null);
                    return super.OnSucceed(result);
                }
            });
    }

    //退出登录
    @Override
    public void doLogout() {
        if (!combineDisposable.isDisposable("doLogout")) return;
        mCase.logout().subscribe(new OnRequestObserver<String>() {

            @Override
            protected void onStart(Disposable d) {
                combineDisposable.addDisposable("doLogout", d);
            }

            @Override
            protected boolean OnSucceed(String result) {
                mView.logoutSuccess();
                return super.OnSucceed(result);
            }
        });
    }


    @Override
    public void getCurrentOrderID() {
        if (!UserCenter.getInstance().isUserLogin()) return;
        if (!combineDisposable.isDisposable("getCurrentOrderID"))
            mCase.getOrderList(OrderStatus.ACCEPTED.getName(), 1)
                    .subscribe(new OnRequestObserver<OrderListResultB>() {
                        @Override
                        protected void onStart(Disposable d) {
                            combineDisposable.addDisposable("getCurrentOrderID", d);
                        }

                        @Override
                        protected boolean OnSucceed(OrderListResultB result) {
                            List<OrderItemB> data = result.getData();
                            int order_id = -1;
                            if (data != null && data.size() > 0) {
                                order_id = data.get(0).getId();
                            }
                            //存储最新的订单id
//                        SharePreferenceCenter center = SharePreferenceCenter.getInstance();
//                        AppSharePreB appShareP = center.getAppShareP();
//                        appShareP.setOrderId(order_id);
//                        center.setAppShareP(appShareP);
                            return true;
                        }


                    });
    }

    @Override
    public void setAllRead() {
        if (!combineDisposable.isDisposable("setAllRead"))
            mCase.setAllRead().subscribe(new OnRequestObserver<String>() {

                @Override
                protected void onStart(Disposable d) {
                    combineDisposable.addDisposable("setAllRead", d);
                }

                @Override
                protected boolean OnSucceed(String result) {
                    mView.setAllReadSuccess();
                    return super.OnSucceed(result);
                }
            });
    }

    /**
     * 获取消息列表，遍历是否有未读消息，显示红点
     */
    @Override
    public void getMessageList() {
        if (!combineDisposable.isDisposable("getMessageList"))
            mCaseV2.getMessageList(0, 50).subscribe(new OnRequestListListener<MessageCenterB>() {


                @Override
                protected void onStart(Disposable d) {
                    combineDisposable.addDisposable("getMessageList", d);
                }

                @Override
                protected boolean OnSucceed(PageB<MessageCenterB> result) {
                    List<MessageCenterB> data = result.getData();
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            //不为空就是已经读了
                            if (data.get(i).getRead_at() == null) {
                                mView.showMessageRedDot(true);
                                break;
                            }
                            if (i == data.size() - 1) {
                                mView.showMessageRedDot(false);
                            }
                        }

                    }
                    return super.OnSucceed(result);
                }
            });
    }
}