package com.weimu.chewu.origin.list.mvp;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.weimu.chewu.origin.list.RecyclerViewDelegatorV2;
import com.weimu.chewu.origin.list.mvp.refresh.RefreshLayout;
import com.weimu.chewu.widget.MultiStateView;

import java.util.List;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 01:33
 * Description:
 */
public class RecyclerDelegate<H, B> implements UniversalListAction<B> {

    private BaseRecyclerWithFooterAdapter<H, B> adapter;
    private Context context;
    private MultiStateView multiStateView;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;


    //分页控制
    public int mPage = 1;
    public int mPageSize = 10;


    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    private RecyclerViewDelegatorV2.LAYOUT_MANAGER_TYPE layoutManagerType = RecyclerViewDelegatorV2.LAYOUT_MANAGER_TYPE.LINEAR;


    //最后一个可见的Item的位置
    private int lastVisibleItemPosition = 0;
    //最后一个的位置
    private int[] lastPositions;

    private OnRecyclerDelegateActionListener onRecyclerDelegateActionListener;

    public RecyclerDelegate(BaseRecyclerWithFooterAdapter<H, B> adapter, Context context, MultiStateView multiStateView, RefreshLayout refreshLayout, RecyclerView recyclerView) {
        this.adapter = adapter;
        this.context = context;
        this.multiStateView = multiStateView;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;

        initRefreshLayout();
        initRecyclerView();
    }

    private void initRefreshLayout() {
        refreshLayout.setOnHeaderRefreshListener(new RefreshLayout.OnHeaderRefreshListener() {
            @Override
            public void onFresh() {
                if (onRecyclerDelegateActionListener != null)
                    onRecyclerDelegateActionListener.beginHeaderRefreshing();
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        //上拉刷新
        //上拉刷新
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (visibleItemCount > 0 && state == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1) {
                    if (adapter.getDataList().size() >= mPageSize && adapter.getState() != BaseRecyclerWithFooterAdapter.State.TheEnd)
                        if (onRecyclerDelegateActionListener != null)
                            onRecyclerDelegateActionListener.beginLoadNextPage();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //上拉为正  下拉为负
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                switch (layoutManagerType) {
                    case LINEAR:
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        break;
                    case GRID:
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                        break;
                    case STAGGERED_GRID:
                        StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                        if (lastPositions == null)
                            lastPositions = new int[manager.getSpanCount()];
                        lastVisibleItemPosition = findMax(manager.findLastVisibleItemPositions(lastPositions));
                        break;
                }
            }
        });
    }

    /**
     * 找出数组中最大值
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 获取排序管理者
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        return recyclerView.getLayoutManager();
    }

    public int getDefaultPage() {
        mPage = 1;
        return mPage;
    }


    @Override
    public void loadFirstPage(List<B> data) {
        if (data == null || data.size() == 0) {
            mPage = 1;
            adapter.clearDataList();
            return;
        }
        mPage++;
        adapter.setDataToAdapter(data);
        recyclerView.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public void loadNextPage(List<B> data) {
        mPage++;
        adapter.addItems(data);
    }

    @Override
    public void beginRefreshAnimation() {
        refreshLayout.beginRefreshAnimation();
    }

    @Override
    public void endRefreshAnimation() {
        refreshLayout.endRefreshAnimation();
    }


    @Override
    public void showErrorView() {
        showErrorView(-1);
    }

    @Override
    public void showErrorView(final int height) {
        //特殊处理  使用了列表的空视图 就不用多视图的空视图
        adapter.clearDataList();
        adapter.hideEmpty();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!adapter.showError(height))
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }
        }, 300);
    }

    @Override
    public void showEmptyView() {
        showEmptyView(-1);
    }


    @Override
    public void showEmptyView(final int height) {
        //特殊处理  使用了列表的空视图 就不用多视图的空视图
        adapter.clearDataList();
        adapter.hideError();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!adapter.showEmpty(height))
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        }, 300);
    }

    @Override
    public void showContentView() {
        adapter.hideEmpty();
        adapter.hideError();
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }


    @Override
    public void showDefaultFooter() {
        adapter.setState(BaseRecyclerWithFooterAdapter.State.Idle);
    }

    @Override
    public void showLoadingFooter() {
        adapter.setState(BaseRecyclerWithFooterAdapter.State.Loading);
    }

    @Override
    public void showNotMoreFooter() {
        adapter.setState(BaseRecyclerWithFooterAdapter.State.TheEnd);
    }

    @Override
    public void showHideFooter() {
        adapter.setState(BaseRecyclerWithFooterAdapter.State.Hide);
    }


    @Override
    public void hideHeader() {
        adapter.hideHeader();
    }

    //delegate 对外的 view操作接口
    public interface OnRecyclerDelegateActionListener {
        //下拉刷新
        void beginHeaderRefreshing();

        //上拉刷新
        void beginLoadNextPage();
    }

    public void setOnRecyclerDelegateActionListener(OnRecyclerDelegateActionListener onRecyclerDelegateActionListener) {
        this.onRecyclerDelegateActionListener = onRecyclerDelegateActionListener;
    }
}
