package com.weimu.chewu.module.loginx;

import android.text.TextUtils;
import android.widget.TextView;

import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.LoginCase;
import com.weimu.universalib.utils.AuthUtils;
import com.weimu.universalib.utils.StringUtils;

import io.reactivex.Observable;

/**
 * Author:你需要一台永动机
 * Date:2018/4/21 11:26
 * Description:
 */
public class LoginCaseImpl implements LoginCase {


    @Override
    public Observable<NormalResponseB<UserB>> loginReq(String phone, String password) {
//        //此处能值的检查
//        String checkResult = checkAllValues(phone, password);
//        if (!TextUtils.isEmpty(checkResult)) {
//            //利用Rx返回错误
//            return Observable.error(new IllegalArgumentException(checkResult));
//        }
        return RetrofitClient.getDefault()
                .create(LoginCase.class)
                .loginReq(phone, password)
                .compose(RxSchedulers.<NormalResponseB<UserB>>toMain());
    }


}
