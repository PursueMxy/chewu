package com.weimu.chewu.module.message;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.MessageCenterB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.module.contract_service.ContactServiceContract;
import com.weimu.chewu.module.contract_service.ContactServicePresenterImpl;
import com.weimu.chewu.module.contract_service.ContractServiceActivity;
import com.weimu.chewu.module.contract_service.ContractServiceDetailActivity;
import com.weimu.chewu.module.contract_service.adapter.ContactServiceAdapter;
import com.weimu.chewu.module.message.adapter.MessageCenterAdapter;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerMVPAdapter;
import com.weimu.chewu.origin.list.mvp.RecyclerDelegate;
import com.weimu.chewu.origin.list.mvp.refresh.NativeRefreshLayoutImp;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.MultiStateView;
import com.weimu.chewu.widget.ToolBarManager;

import butterknife.BindView;

public class MessageCenterActivity extends BaseActivity implements MessageCenterContract.mView {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_message_center;
    }


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MessageCenterActivity.class);
        return intent;
    }

    private MessageCenterContract.mPresenter mPresenter;
    private MessageCenterAdapter mAdapter;
    private RecyclerDelegate<BaseB, MessageCenterB> listDelegate;

    @BindView(R.id.mMultiStateView)
    MultiStateView mMultiStateView;

    @BindView(R.id.mRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new MessageCenterPresenterImpl(this);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        initList();
        requestFirstPage();
    }

    private void initList() {
        mAdapter = new MessageCenterAdapter(getContext());
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

        mPresenter.setListAction(listDelegate);
        listDelegate.mPageSize = 50;//这个根本不是分页

    }


    private void requestFirstPage() {
        mPresenter.getMessageList(listDelegate.getDefaultPage(), listDelegate.mPageSize);
    }

    private void requestNextPage() {
        mPresenter.getMessageList(listDelegate.mPage, listDelegate.mPageSize);
    }


    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setTitle("消息提醒")
                .setNavigationIcon(R.drawable.back);
    }
}
