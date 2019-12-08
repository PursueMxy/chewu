package com.weimu.chewu.module.login.check_register;

import android.text.TextUtils;

import com.weimu.chewu.backend.bean.CheckRegisterInfo;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.CheckRegisterCase;
import com.weimu.chewu.backend.remote.LoginCase;
import com.weimu.universalib.utils.AuthUtils;
import com.weimu.universalib.utils.StringUtils;

import io.reactivex.Observable;

/**
 * Created by huangjinfu on 18/5/2.
 */

public class CheckRegisterCaseImpl implements CheckRegisterCase {
    @Override
    public Observable<NormalResponseB<CheckRegisterInfo>> checkRegister(String phone) {
        //此处能值的检查
        String checkResult = checkAllValues(phone);
        if (!TextUtils.isEmpty(checkResult)) {
            //利用Rx返回错误
            return Observable.error(new IllegalArgumentException(checkResult));
        }
        return RetrofitClient.getDefault()
                .create(CheckRegisterCase.class)
                .checkRegister(phone)
                .compose(RxSchedulers.<NormalResponseB<CheckRegisterInfo>>toMain());
    }

    //检查所有编辑框输入的值
    private String checkAllValues(String account) {
        if (TextUtils.isEmpty(account)) {
            return "手机号为空";
        }

        if (!AuthUtils.isMobileNo(account)) {
            return "请确保手机号格式输入正确";
        }
        return null;
    }
}
