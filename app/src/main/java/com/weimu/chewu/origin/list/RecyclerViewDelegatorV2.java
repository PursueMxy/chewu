package com.weimu.chewu.origin.list;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.observer.OnRequestListListener;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.MultiStateView;
import com.weimu.universalib.utils.SpannableUtils;
import com.weimu.universalib.utils.ViewsUtils;
import com.weimu.universalib.utils.WindowsUtils;

import java.util.List;

/**
 * @author 艹羊
 * @project Checaiduo_Android
 * @date 2017/5/27 下午4:23
 * @description 以recyclerView为主，实现下拉，上拉功能的集成者
 */

public class RecyclerViewDelegatorV2<T extends BaseB> {
    private RecyclerView mRecyclerView;//列表
    private PullRefreshLayout mRefreshLayout;//第三方下拉控件
    private LoadingFooter mFooter;//上拉显示更多
    private MultiStateView mMultiStateView;//多状态视图

    private BaseActivity mContext;

    private OnLoadFirstPageLister<T> mOnLoadFirstPageLister;
    private boolean isDefault = true;


    //paging control
    private int mPage = 1;
    private int mPageSize = 10;
    private BaseRecyclerAdapter<T> mAdapter;
    private CommandV2<T> mCommand;

    private int ITEM_MAX_NUMBER = Integer.MAX_VALUE;//设置最大的item数量限制

    //最后一个的位置
    private int[] lastPositions;
    //最后一个可见的item的位置
    private int lastVisibleItemPosition;
    private boolean isSteadyListView = false;//是否为固定列表
    private boolean isAddFooter = true;//是否要添加底部

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    private LAYOUT_MANAGER_TYPE layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;


    private boolean isOpenEmptyInRecyclerMode = false;
    private View emptyView;


    public RecyclerViewDelegatorV2(BaseActivity context, MultiStateView stateView, PullRefreshLayout refreshLayout, RecyclerView listView, BaseRecyclerAdapter<T> adapter, CommandV2<T> mCommand) {
        this.mContext = context;
        this.mMultiStateView = stateView;
        this.mRefreshLayout = refreshLayout;
        this.mRecyclerView = listView;
        mFooter = new LoadingFooter(mContext);

        mAdapter = adapter;
        this.mCommand = mCommand;

        initRefreshLayout();
        initRecyclerView();
        initLayoutType();
        initMultiStateView();
    }

    public RecyclerViewDelegatorV2(BaseActivity context, MultiStateView stateView, PullRefreshLayout refreshLayout, RecyclerView listView, BaseRecyclerAdapter<T> adapter, CommandV2<T> mCommand, boolean isOpenEmptyInRecyclerMode) {
        this.mContext = context;
        this.mMultiStateView = stateView;
        this.mRefreshLayout = refreshLayout;
        this.mRecyclerView = listView;
        mFooter = new LoadingFooter(mContext);
        this.isOpenEmptyInRecyclerMode = isOpenEmptyInRecyclerMode;

        mAdapter = adapter;
        this.mCommand = mCommand;

        initRefreshLayout();
        initRecyclerView();
        initLayoutType();
        initMultiStateView();
    }


    private void initMultiStateView() {
        View emptyView = mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR);
        TextView tvNetwork = (TextView) emptyView.findViewById(R.id.tv_network);

        //tv protocol
        SpannableStringBuilder strSpan = new SpannableStringBuilder("网络异常，请重新载入");
        SpannableUtils.NoLineSpan noLineSpan = new SpannableUtils.NoLineSpan(mContext) {
            @Override
            public void onClick(View widget) {
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                beginHeaderRefreshing();
            }
        };
        strSpan.setSpan(noLineSpan, 6, tvNetwork.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvNetwork.setText(strSpan);
        tvNetwork.setMovementMethod(LinkMovementMethod.getInstance());//必须设置才能点击
    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                beginHeaderRefreshing();
            }
        });
        //下拉刷新控件的配色
        mRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.colorPrimary));
    }


    private void initRecyclerView() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        mRecyclerView.setAdapter(mAdapter);

        //上拉刷新
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (visibleItemCount > 0 && state == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1) {
                    if (mAdapter.getDataListCount() >= mPageSize && mFooter.getState() != LoadingFooter.State.TheEnd)
                        loadNextPage();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //上拉为正  下拉为负
                RecyclerView.LayoutManager layoutManager = getLayoutManager();
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
        //foot
        if (isAddFooter)
            mAdapter.addFootView(mFooter.getView());

        if (isOpenEmptyInRecyclerMode) {
            emptyView = LayoutInflater.from(mContext).inflate(R.layout.content_list_state_empty, mRecyclerView, false);
            ViewGroup.LayoutParams layoutParams = emptyView.getLayoutParams();
            layoutParams.height = WindowsUtils.dip2px(300);
            emptyView.setLayoutParams(layoutParams);

            ViewsUtils.hideAllViews(emptyView);
            mAdapter.addEmptyView(emptyView);
        }

    }

    private void initLayoutType() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
        } else if (layoutManager instanceof GridLayoutManager) {
            layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
        } else {
            throw new RuntimeException(
                    "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
        }
    }

    /**
     * @description 进入页面初次手动开启加载请求
     */
    public void beginHeaderRefreshing() {
        beginRefreshAnimation();
        loadFirstPage();
    }


    /**
     * @description 下拉刷新
     */
    private void loadFirstPage() {
        if (!isDefault && mOnLoadFirstPageLister != null) {
            mOnLoadFirstPageLister.onLoad(mCommand);
        }
        mFooter.setState(LoadingFooter.State.Idle);
        mPage = 1;
        mCommand.execute(mPage + "", mPageSize + "", new OnRequestListListener<T>(mContext) {

            @Override
            protected boolean OnSucceed(PageB<T> value) {
                super.onNext(value);
                endRefreshAnimation();//下拉刷新的动画必须执行完成，才能进行视图切换，否则偶尔会失效！！！
                if (value.getCode() != 200) {
                    mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    return true;
                }
                isDefault = false;
                //数据
                List<T> dataList = value.getData();
                if (dataList.size() == 0) {
                    mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                } else {
                    mFooter.setState(dataList.size() < mPageSize ? LoadingFooter.State.TheEnd : LoadingFooter.State.Idle);
                    mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    mAdapter.setDataToAdapter(dataList);
                    mPage++;

                    mRecyclerView.smoothScrollToPosition(0);
                    hideEmptyView();
                }
                return super.OnSucceed(value);
            }

            @Override
            protected boolean onFailure(String message) {
                isDefault = false;
                endRefreshAnimation();
                mFooter.setState(LoadingFooter.State.Idle);
                mAdapter.setDataToAdapter(null);
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                return super.onFailure(message);
            }

        });
    }


    /**
     * @description 载入更多
     */
    private void loadNextPage() {
        hideEmptyView();
        if (isSteadyListView) {
            return;
        }
        mFooter.setState(LoadingFooter.State.Loading);
        mCommand.execute(mPage + "", mPageSize + "", new OnRequestListListener<T>(mContext) {
            @Override
            protected boolean OnSucceed(PageB<T> value) {
                if (value.getCode() != 200) {
                    mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    return true;
                }
                if (mAdapter.getDataListCount() > ITEM_MAX_NUMBER) {
                    mFooter.setState(LoadingFooter.State.TheEnd);
                    return true;
                }
                //数据
                List<T> dataList = value.getData();
                if (value.getData().size() != 0) {
                    mPage++;
                    mAdapter.addData(dataList);
                    checkReceiveData(dataList);
                    checkLocalListData();
                }
                return super.OnSucceed(value);
            }

            @Override
            protected boolean onFailure(String message) {
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                return super.onFailure(message);
            }

        });
    }


    /**
     * 检查接收到的数据
     * 如果size小于pageSize则说明没有更多了
     */
    private void checkReceiveData(List<T> dataList) {
        mFooter.setState(dataList.size() < mPageSize ? LoadingFooter.State.TheEnd : LoadingFooter.State.Idle);
    }

    /**
     * 检查本地列表的数据
     */
    private void checkLocalListData() {
        mMultiStateView.setViewState(mAdapter.getItemCount() <= 1 ? MultiStateView.VIEW_STATE_EMPTY : MultiStateView.VIEW_STATE_CONTENT);
    }


    public void setOnLoadFirstPageLister(OnLoadFirstPageLister listener) {
        mOnLoadFirstPageLister = listener;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * 获取排序管理者
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    /**
     * 设置排序方式
     */
    public void setLayoutManager(@NonNull RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
        //解决GridView加载更多的视图错位问题
        if (!(layout instanceof GridLayoutManager)) {
            return;
        }
        final GridLayoutManager gridLayoutManager = (GridLayoutManager) layout;
        //设置recylerview的一个item占多大宽度，这边使用在gridlayoutmanager,
        // 打比方每行显示3个item,当设置这个方法时，可以使一个特定的item显示初始的的几个item的宽度
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanSize = 1;
                if (mAdapter.isAddHead() && position == 0) {
                    //如果是头部，那么宽度占满横向
                    spanSize = gridLayoutManager.getSpanCount();
                }

                if (mAdapter.isAddFoot() && position == mAdapter.getDataListCount()) {
                    //如果是底部，那么宽度占满横向
                    spanSize = gridLayoutManager.getSpanCount();
                }
                return spanSize;
            }
        });

    }

    public void setMaxItemNumber(int maxItemNumber) {
        this.ITEM_MAX_NUMBER = maxItemNumber;
    }


    public void setSteadyListView(boolean steadyListView) {
        isSteadyListView = steadyListView;
    }

    //设置起始页
    public void setPage(int page) {
        this.mPage = page;
    }

    //设置每页的数量
    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }


    // 增加底部
    public void addFooterView(View view) {
        mAdapter.addFootView(view);
    }

    //增加头部
    public void addHeaderView(View view) {
        mAdapter.addHeadView(view);
    }

    /**
     * 增加ItemDecoration
     */
    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }


    private void beginRefreshAnimation() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void endRefreshAnimation() {

        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


    public interface OnLoadFirstPageLister<T> {
        void onLoad(CommandV2<T> mCommand);
    }


    //点击事件
    private OnItemClickListener<T> onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void OnItemClick(T item, int position);
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


    private void showEmptyView() {
        if (!isOpenEmptyInRecyclerMode) return;
        if (emptyHeight == -1) return;
        ViewsUtils.showAllViews(emptyView);
        ViewGroup.LayoutParams layoutParams = emptyView.getLayoutParams();
        layoutParams.height = emptyHeight;
        emptyView.setLayoutParams(layoutParams);
        ViewsUtils.showAllViews(emptyView);
    }

    private int emptyHeight = -1;

    private void hideEmptyView() {
        if (!isOpenEmptyInRecyclerMode) return;
        if (emptyHeight == -1)
            emptyHeight = emptyView.getHeight();
        ViewsUtils.hideAllViews(emptyView);
        ViewGroup.LayoutParams layoutParams = emptyView.getLayoutParams();
        layoutParams.height = 0;
        emptyView.setLayoutParams(layoutParams);
        ViewsUtils.hideAllViews(emptyView);

    }
}
