package com.weimu.chewu.origin.list.mvp;

import java.util.List;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 01:27
 * Description:
 */
public interface UniversalListAction<B> {

    void showErrorView();

    void showErrorView(int height);

    void showEmptyView();

    void showEmptyView(int height);

    void showContentView();

    void beginRefreshAnimation();

    void endRefreshAnimation();

    void showDefaultFooter();

    void showLoadingFooter();

    void showNotMoreFooter();

    void showHideFooter();

    void loadFirstPage(List<B> data);

    void loadNextPage(List<B> data);

    void hideHeader();
}
