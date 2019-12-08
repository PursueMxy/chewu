package com.weimu.chewu.module.id_code;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.ContactServiceCase;
import com.weimu.chewu.backend.remote.RegisterCase;
import com.weimu.chewu.backend.remote.RegisterCodeCase;

import io.reactivex.Observable;

/**
 * Created by huangjinfu on 18/5/16.
 */

public class IdCodeCaseImpl implements RegisterCodeCase {

    @Override
    public Observable<NormalResponseB<String>> getCode(String phone) {
        return RetrofitClient.getDefault()
                .create(RegisterCodeCase.class)
                .getCode(phone)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }

    @Override
    public Observable<NormalResponseB<String>> checkIdCode(String phone, String code) {
        return RetrofitClient.getDefault()
                .create(RegisterCodeCase.class)
                .checkIdCode(phone, code)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }
}
