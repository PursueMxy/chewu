package com.weimu.chewu.module.message;

import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.MessageCenterB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.ContactServiceCase;
import com.weimu.chewu.backend.remote.MessageCenterCase;
import com.weimu.chewu.origin.center.UserCenter;

import io.reactivex.Observable;

/**
 * Created by huangjinfu on 18/5/14.
 */

public class MessageCenterCaseImpl implements MessageCenterCase {
    @Override
    public Observable<PageB<MessageCenterB>> getMessageList(int page, int pageSize) {
        return RetrofitClient.getDefault()
                .create(MessageCenterCase.class)
                .getMessageList(page, pageSize)
                .compose(RxSchedulers.<PageB<MessageCenterB>>toMain());
    }
}
