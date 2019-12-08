package com.weimu.chewu.origin.list.mvp.refresh;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import com.weimu.chewu.AppData;
import com.weimu.chewu.R;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 02:02
 * Description:
 */
public class NativeRefreshLayoutImp extends RefreshLayout {


    private SwipeRefreshLayout refreshLayout;

    public NativeRefreshLayoutImp(SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;

        //下拉刷新控件的配色
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(AppData.getContext(), R.color.colorPrimary));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onHeaderRefreshListener != null) onHeaderRefreshListener.onFresh();
            }
        });
    }


    @Override
    public void beginRefreshAnimation() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void endRefreshAnimation() {
        if (refreshLayout == null) return;
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
            }
        }, 500);
    }
}
