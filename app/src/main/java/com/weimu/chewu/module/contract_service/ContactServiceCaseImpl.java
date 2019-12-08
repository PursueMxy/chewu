package com.weimu.chewu.module.contract_service;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.CustomerPhoneB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.ContactServiceCase;

import io.reactivex.Observable;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 00:14
 * Description:
 */
public class ContactServiceCaseImpl implements ContactServiceCase {


    @Override
    public Observable<PageB<ContactServiceB>> getQuestionsList(int page, int pageSize) {
        return RetrofitClient.getDefault()
                .create(ContactServiceCase.class)
                .getQuestionsList(page, pageSize)
                .compose(RxSchedulers.<PageB<ContactServiceB>>toMain());
    }

    @Override
    public Observable<NormalResponseB<CustomerPhoneB>> getServicePhone() {
        return RetrofitClient.getDefault()
                .create(ContactServiceCase.class)
                .getServicePhone()
                .compose(RxSchedulers.<NormalResponseB<CustomerPhoneB>>toMain());
    }
}
