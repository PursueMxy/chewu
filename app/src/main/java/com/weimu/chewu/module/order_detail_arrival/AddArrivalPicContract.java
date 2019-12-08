package com.weimu.chewu.module.order_detail_arrival;

import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

/**
 * Created by huangjinfu on 18/9/2.
 */

public interface AddArrivalPicContract {
    interface View extends BaseView {
        public void onUploadPicSuccess();
        public void onUploadPicFail(String message);

    }


    interface Presenter extends BasePresenter {
        public void upLoadPic(int order_id, String car_images);
    }
}
