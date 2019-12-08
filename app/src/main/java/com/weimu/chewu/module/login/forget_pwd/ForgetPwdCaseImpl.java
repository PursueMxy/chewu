package com.weimu.chewu.module.login.forget_pwd;

import android.text.TextUtils;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.ForgetPwdCase;
import com.weimu.chewu.backend.remote.LoginCase;
import com.weimu.universalib.utils.AuthUtils;
import com.weimu.universalib.utils.StringUtils;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

/**
 * Created by huangjinfu on 18/5/2.
 */

public class ForgetPwdCaseImpl implements ForgetPwdCase {

    @Override
    public Observable<NormalResponseB<String>> resetPassword(String phone, String code, String new_password) {
        //此处能值的检查
        String checkResult = checkAllValues(new_password);
        if (!TextUtils.isEmpty(checkResult)) {
            //利用Rx返回错误
            return Observable.error(new IllegalArgumentException(checkResult));
        }
        return RetrofitClient.getDefault()
                .create(ForgetPwdCase.class)
                .resetPassword(phone, code, new_password)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }

    //检查所有编辑框输入的值
    private String checkAllValues(String pwd) {

        if (TextUtils.isEmpty(pwd)) {
            return "密码输入为空";
        }

        if (!StringUtils.isValidateOr(pwd)) {
            return "请确保密码是6~20位字母或数字的组成";
        }
        return null;
    }
}
