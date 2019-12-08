package com.weimu.chewu.module.register;

import com.weimu.chewu.origin.mvp.BasePresenter;
import com.weimu.chewu.origin.mvp.BaseView;

import java.util.ArrayList;

import okhttp3.RequestBody;

/**
 * Created by huangjinfu on 18/4/22.
 */

public interface RegisterContract {

    interface View extends BaseView {

        void showSuccessResult();
    }


    interface Presenter extends BasePresenter {
        void register(String code, String phone, String password, String name, String passport, String passport_images,String driver_license_images, String driving_license_images);

//        void register(String code, String phone,String password,String name,String passport,String passport_images,String driver_license_images,String driving_license_images);
    }
}
