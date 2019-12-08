package com.weimu.chewu.module.order.ed;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.module.order.OrderListContract;
import com.weimu.chewu.module.order.OrderListPresenterImpl;
import com.weimu.chewu.module.order.TabRefreshAction;
import com.weimu.chewu.module.order.adapter.OrderListAdapter;
import com.weimu.chewu.module.order_detail_arrival.OrderDetailArrivalV2Activity;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerMVPAdapter;
import com.weimu.chewu.origin.list.mvp.RecyclerDelegate;
import com.weimu.chewu.origin.list.mvp.refresh.NativeRefreshLayoutImp;
import com.weimu.chewu.origin.view.BaseFragment;
import com.weimu.chewu.type.OrderStatus;
import com.weimu.chewu.widget.MultiStateView;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/3/8 11:25
 * Description:
 */

public class OrderEdListFragment extends BaseFragment implements OrderListContract.View, TabRefreshAction {
    @BindView(R.id.multiStateView)
    MultiStateView mMultiStateView;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;


    public static OrderEdListFragment newInstance() {
        return new OrderEdListFragment();
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_order_ed;
    }


    private OrderListAdapter mAdapter;
    private RecyclerDelegate<BaseB, OrderItemB> listDelegate;
    private OrderListContract.Presenter mPresenter;


    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        initDelegatoer();
        requestFirstPage();
    }


    private void initDelegatoer() {
        //适配器
        mAdapter = new OrderListAdapter(getContext());
        mAdapter.setOnItemClick(new BaseRecyclerMVPAdapter.OnItemClick<OrderItemB>() {
            @Override
            public void onClick(OrderItemB item, int position) {
                startActivity(OrderDetailArrivalV2Activity.newIntent(getContext(), item));
            }
        });

        //列表通用器
        listDelegate = new RecyclerDelegate<>(mAdapter, getContext(), mMultiStateView, new NativeRefreshLayoutImp(mRefreshLayout), mRecyclerView);
        listDelegate.setOnRecyclerDelegateActionListener(new RecyclerDelegate.OnRecyclerDelegateActionListener() {
            @Override
            public void beginHeaderRefreshing() {
                requestFirstPage();
            }

            @Override
            public void beginLoadNextPage() {
                requestNextPage();
            }
        });

        //presenter
        mPresenter = new OrderListPresenterImpl(this, listDelegate);
    }


    private void requestFirstPage() {
        mPresenter.getOrderList(OrderStatus.SUCCEED.getName(), listDelegate.getDefaultPage());
    }

    private void requestNextPage() {
        mPresenter.getOrderList(OrderStatus.SUCCEED.getName(), listDelegate.mPage);
    }


    @Override
    public void onRefresh() {
        requestFirstPage();
    }


}
