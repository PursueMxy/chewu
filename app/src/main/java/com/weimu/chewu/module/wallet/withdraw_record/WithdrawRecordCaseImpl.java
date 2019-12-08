package com.weimu.chewu.module.wallet.withdraw_record;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.WithdrawRecordB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.ContactServiceCase;
import com.weimu.chewu.backend.remote.WithdrawRecordCase;

import io.reactivex.Observable;

/**
 * Created by huangjinfu on 18/5/11.
 */

public class WithdrawRecordCaseImpl implements WithdrawRecordCase {
    @Override
    public Observable<NormalResponseB<PageB<WithdrawRecordB>>> getWithdrawRecordList(int page, int pageSize) {
        return RetrofitClient.getDefault()
                .create(WithdrawRecordCase.class)
                .getWithdrawRecordList(page, pageSize)
                .compose(RxSchedulers.<NormalResponseB<PageB<WithdrawRecordB>>>toMain());
    }


}
