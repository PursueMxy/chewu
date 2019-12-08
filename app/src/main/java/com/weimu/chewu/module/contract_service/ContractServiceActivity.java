package com.weimu.chewu.module.contract_service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.module.contract_service.adapter.ContactServiceAdapter;
import com.weimu.chewu.module.station.StationActivity;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerMVPAdapter;
import com.weimu.chewu.origin.list.mvp.RecyclerDelegate;
import com.weimu.chewu.origin.list.mvp.refresh.NativeRefreshLayoutImp;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.MultiStateView;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.universalib.utils.IntentUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 联系客服
 */
public class ContractServiceActivity extends BaseActivity implements ContactServiceContract.mView {
    private String phone;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_contract_service;
    }


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ContractServiceActivity.class);
        return intent;
    }

    private ContactServiceContract.mPresenter mPresenter;
    private ContactServiceAdapter mAdapter;
    private RecyclerDelegate<BaseB, ContactServiceB> listDelegate;

    @BindView(R.id.mMultiStateView)
    MultiStateView mMultiStateView;

    @BindView(R.id.mRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new ContactServicePresenterImpl(this);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        getCustomerPhone();
        initList();
        requestFirstPage();
    }

    @OnClick(R.id.contract_service_tv_connect)
    public void onConnectClick() {
        IntentUtils.gotoDialog(getContext(), phone);

    }
    private void getCustomerPhone(){
        mPresenter.getServicePhone();
    }

    private void initList() {
        mAdapter = new ContactServiceAdapter(getContext());
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
        mAdapter.setOnItemClick(new BaseRecyclerMVPAdapter.OnItemClick<ContactServiceB>() {
            @Override
            public void onClick(ContactServiceB item, int position) {
                Intent intent = new Intent(ContractServiceActivity.this, ContractServiceDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("contactServiceB", item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    private void requestFirstPage() {
        mPresenter.getQuestionsList(listDelegate.getDefaultPage(), listDelegate.mPageSize);
    }

    private void requestNextPage() {
        mPresenter.getQuestionsList(listDelegate.mPage, listDelegate.mPageSize);
    }


    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setTitle("联系客服")
                .setNavigationIcon(R.drawable.back);
    }

    @Override
    public void getPhone(String phone) {
        this.phone = phone;
    }
}
