package com.weimu.chewu.module.contract_service;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.CustomerPhoneB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestListListener;
import com.weimu.chewu.backend.http.observer.OnSingleRequestObserver;
import com.weimu.chewu.backend.remote.ContactServiceCase;
import com.weimu.chewu.origin.list.mvp.UniversalListAction;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 00:17
 * Description:
 */
public class ContactServicePresenterImpl implements ContactServiceContract.mPresenter {
    private CombineDisposable combineDisposable = new CombineDisposable();


    private ContactServiceContract.mView mView;
    private ContactServiceCase mCase;
    private UniversalListAction<ContactServiceB> listAction;

    public ContactServicePresenterImpl(ContactServiceContract.mView mView) {
        this.mView = mView;
        mCase = new ContactServiceCaseImpl();
    }

    @Override
    public void setListAction(UniversalListAction<ContactServiceB> listAction) {
        this.listAction = listAction;
    }


    @Override
    public void getQuestionsList(int index, final int pageSize) {
        final boolean isFirstPage = index == 1;
        mCase.getQuestionsList(index, pageSize).subscribe(new OnRequestListListener<ContactServiceB>() {
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
            protected boolean OnSucceed(PageB<ContactServiceB> value) {
                List<ContactServiceB> data = value.getData();
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
    public void getServicePhone() {
        mCase.getServicePhone().subscribe(new OnSingleRequestObserver<NormalResponseB<CustomerPhoneB>>() {
            @Override
            protected void onSuccess(NormalResponseB<CustomerPhoneB> result) {
                super.onSuccess(result);
                mView.getPhone(result.getData().getPhone());
            }
        });
    }


    @Override
    public void destroy() {
        combineDisposable.dispose();
    }
}
