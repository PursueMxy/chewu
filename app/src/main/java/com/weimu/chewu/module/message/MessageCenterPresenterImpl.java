package com.weimu.chewu.module.message;

import com.weimu.chewu.backend.bean.MessageCenterB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestListListener;
import com.weimu.chewu.backend.remote.MessageCenterCase;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.list.mvp.UniversalListAction;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by huangjinfu on 18/5/14.
 */

public class MessageCenterPresenterImpl implements MessageCenterContract.mPresenter {
    private CombineDisposable combineDisposable = new CombineDisposable();


    private MessageCenterContract.mView mView;
    private MessageCenterCase mCase;
    private UniversalListAction<MessageCenterB> listAction;

    public MessageCenterPresenterImpl(MessageCenterContract.mView mView) {
        this.mView = mView;
        mCase = new MessageCenterCaseImpl();
    }

    @Override
    public void setListAction(UniversalListAction<MessageCenterB> listAction) {
        this.listAction = listAction;
    }


    @Override
    public void getMessageList(int index, final int pageSize) {
        if (!UserCenter.getInstance().isUserLogin()) return;
        final boolean isFirstPage = index == 1;
//        mCase.getMessageList(index, pageSize).subscribe(new OnSingleRequestObserver<PageB<MessageCenterB>>() {
//            @Override
//            protected void onSuccess(PageB<MessageCenterB> result) {
//                List<MessageCenterB> data = result.getData();
//
//                super.onSuccess(result);
//            }
//        });
        boolean isDisposable = combineDisposable.isDisposable("getMessage");
        if(!isDisposable)return;
        mCase.getMessageList(index, pageSize).subscribe(new OnRequestListListener<MessageCenterB>() {
//        if(!isDisposable)return;

            @Override
            protected void onStart(Disposable d) {
                super.onStart(d);
                combineDisposable.addDisposable("getMessage", d);
                listAction.showContentView();
                if (isFirstPage)
                    listAction.beginRefreshAnimation();
                else
                    listAction.endRefreshAnimation();
            }

            @Override
            protected boolean OnSucceed(PageB<MessageCenterB> value) {
                List<MessageCenterB> data = value.getData();
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
                return super.OnSucceed(value);
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
