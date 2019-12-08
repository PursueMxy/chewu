package com.weimu.chewu.module.order;

import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.bean.OrderListResultB;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.OrderListCase;
import com.weimu.chewu.origin.list.mvp.UniversalListAction;
import com.weimu.universalib.utils.WindowsUtils;

import java.util.List;

import io.reactivex.disposables.Disposable;

import static com.weimu.universalib.OriginAppData.getContext;

/**
 * Author:你需要一台永动机
 * Date:2018/5/19 14:56
 * Description:
 */
public class OrderListPresenterImpl implements OrderListContract.Presenter {

    private CombineDisposable combineDisposable = new CombineDisposable();

    private OrderListContract.View mView;
    private OrderListCase mCase;
    private UniversalListAction<OrderItemB> listAction;


    public OrderListPresenterImpl(OrderListContract.View mView, UniversalListAction<OrderItemB> listAction) {
        this.mView = mView;
        mCase = new OrderCaseImpl();
        this.listAction = listAction;
    }


    @Override
    public void getOrderList(String status, int page) {
        boolean disposable = combineDisposable.isDisposable("getOrderList");
        if (!disposable) return;
        final boolean isFirstPage = page == 1;
        final int contentHeight = WindowsUtils.getScreenHeight(getContext()) - WindowsUtils.dip2px(168);//根据不同页面自行计算
        mCase.getOrderList(status, page)
                .subscribe(new OnRequestObserver<OrderListResultB>() {
                    @Override
                    protected void onStart(Disposable d) {
                        combineDisposable.addDisposable("getOrderList", d);
                        listAction.showContentView();
                        if (isFirstPage)
                            listAction.beginRefreshAnimation();
                        else
                            listAction.showLoadingFooter();
                    }

                    @Override
                    protected boolean OnSucceed(OrderListResultB result) {
                        List<OrderItemB> data = result.getData();
                        int pageSize = result.getPer_page();

                        if (isFirstPage) {
                            listAction.endRefreshAnimation();

                            if (data == null || data.size() == 0) {
                                listAction.showHideFooter();
                                listAction.showEmptyView(contentHeight);
                            } else {
                                listAction.loadFirstPage(data);
                                if (data.size() < pageSize) {
                                    listAction.showNotMoreFooter();
                                } else {
                                    listAction.showDefaultFooter();
                                }
                            }

                        } else {
                            if (data == null || data.size() == 0) {
                                listAction.showNotMoreFooter();
                            } else {
                                listAction.loadNextPage(data);
                                listAction.showDefaultFooter();
                            }
                        }
                        return true;

                    }

                    @Override
                    protected boolean onFailure(String message) {
                        if (isFirstPage) {
                            listAction.showErrorView(contentHeight);
                            listAction.endRefreshAnimation();
                        }

                        listAction.showHideFooter();
                        return true;
                    }
                });
    }


    @Override
    public void destroy() {
        combineDisposable.dispose();
    }

}
