package com.weimu.chewu.origin.list.mvp.refresh;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 01:34
 * Description:
 */
public abstract class RefreshLayout {

    public abstract void beginRefreshAnimation();

    public abstract void endRefreshAnimation();

    protected OnHeaderRefreshListener onHeaderRefreshListener;

    public void setOnHeaderRefreshListener(OnHeaderRefreshListener onHeaderRefreshListener) {
        this.onHeaderRefreshListener = onHeaderRefreshListener;
    }

    public interface OnHeaderRefreshListener {
        void onFresh();
    }
}
