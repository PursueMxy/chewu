package com.weimu.chewu.module.register;

import android.text.TextUtils;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.base.PageB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.LoginCase;
import com.weimu.chewu.backend.remote.RegisterCase;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by huangjinfu on 18/4/22.
 */

public class RegisterCaseImpl implements RegisterCase {
    @Override
    public Observable<NormalResponseB<BaseB>> registerReq(String code, String phone, String password, String name, String passport, String passport_images, String driver_license_images, String driving_license_images) {

        //此处能值的检查
        String checkResult = checkAllValues(name, passport);
        if (!TextUtils.isEmpty(checkResult)) {
            //利用Rx返回错误
            return Observable.error(new IllegalArgumentException(checkResult));
        }

        return RetrofitClient.getDefault()
                .create(RegisterCase.class)
                .registerReq(code, phone, password, name, passport, passport_images, driver_license_images, driving_license_images)
                .compose(RxSchedulers.<NormalResponseB<BaseB>>toMain());
    }


    private String checkAllValues(String name, String passport) {
        if (TextUtils.isEmpty(name)) {
            return "请输入姓名";
        }
        if (passport.length() != 18) {
            return "身份证必须是18位";
        }
        return null;
    }
}
