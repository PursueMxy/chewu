package com.weimu.chewu.module.wallet.withdraw_record;

import com.weimu.chewu.backend.bean.WithdrawRecordB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.WithdrawRecordCase;
import com.weimu.chewu.origin.list.mvp.UniversalListAction;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by huangjinfu on 18/5/11.
 */

public class WithdrawRecordPresenterImpl implements WithdrawRecordContract.mPresenter {
    private CombineDisposable combineDisposable = new CombineDisposable();


    private WithdrawRecordContract.mView mView;
    private WithdrawRecordCase mCase;
    private UniversalListAction<WithdrawRecordB> listAction;

    public WithdrawRecordPresenterImpl(WithdrawRecordContract.mView mView) {
        this.mView = mView;
        mCase = new WithdrawRecordCaseImpl();
    }

    @Override
    public void setListAction(UniversalListAction<WithdrawRecordB> listAction) {
        this.listAction = listAction;
    }


    @Override
    public void getRecordList(int index, final int pageSize) {
        final boolean isFirstPage = index == 1;
        mCase.getWithdrawRecordList(index, pageSize).subscribe(new OnRequestObserver<PageB<WithdrawRecordB>>() {
            @Override
            protected void onStart(Disposable d) {
                super.onStart(d);
                listAction.showContentView();
                if (isFirstPage)
                    listAction.beginRefreshAnimation();
                else
                    listAction.endRefreshAnimation();
            }

            @Override
            protected boolean OnSucceed(PageB<WithdrawRecordB> result) {
                List<WithdrawRecordB> data = result.getData();
                if (isFirstPage) {

                    if (data == null || data.size() <= 0) {
                        listAction.showHideFooter();
                        listAction.showEmptyView();
                    } else {
                        listAction.loadFirstPage(data);
                        if (data.size() < pageSize) {
                            listAction.showNotMoreFooter();
                        } else {
                            listAction.showDefaultFooter();
                        }
                    }
                    listAction.endRefreshAnimation();
                } else {
                    if (data == null || data.size() <= 0) {
                        listAction.showNotMoreFooter();
                    } else {
                        listAction.loadNextPage(data);
                        listAction.showDefaultFooter();
                    }
                }
                return super.OnSucceed(result);
            }

            @Override
            protected boolean onFailure(String message) {
                if (isFirstPage) {
                    listAction.showErrorView();
                    listAction.endRefreshAnimation();
                } else {
                    listAction.showDefaultFooter();
                }
                return super.onFailure(message);
            }
        });
    }


    @Override
    public void destroy() {
        combineDisposable.dispose();
    }
}
