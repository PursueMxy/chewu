package com.weimu.chewu.module.order_detail_arrival;

import com.weimu.chewu.backend.bean.OrderListResultB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.AddArrivalPicCase;
import com.weimu.chewu.backend.remote.OrderListCase;

import io.reactivex.Observable;

/**
 * Created by huangjinfu on 18/9/2.
 */

public class AddArrivalPicCaseImpl implements AddArrivalPicCase {
    @Override
    public Observable<NormalResponseB<String>> uploadPic(int order_id, String car_images) {
        return RetrofitClient.getDefault()
                .create(AddArrivalPicCase.class)
                .uploadPic(order_id, car_images)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }
}
