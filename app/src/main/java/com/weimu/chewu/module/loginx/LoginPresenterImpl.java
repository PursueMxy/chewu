package com.weimu.chewu.module.loginx;

import com.weimu.chewu.AppData;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.LoginCase;
import com.weimu.chewu.origin.center.UserCenter;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.disposables.Disposable;

/**
 * Author:你需要一台永动机
 * Date:2018/4/21 11:39
 * Description:
 */
public class LoginPresenterImpl implements LoginContract.Presenter {
    private CombineDisposable combineDisposable = new CombineDisposable();

    private LoginContract.View mView;
    private LoginCase mCase;

    public LoginPresenterImpl(LoginContract.View mView) {
        this.mView = mView;
        mCase = new LoginCaseImpl();
    }

    @Override
    public void login(String phone, String password) {
        //mView传入 有圆形进度条  不传入 不会显示圆形进度条
        mCase.loginReq(phone, password).subscribe(new OnRequestObserver<UserB>(mView) {
            //开始
            @Override
            protected void onStart(Disposable d) {
                super.onStart(d);
            }

            //成功
            @Override
            protected boolean OnSucceed(UserB result) {
                //视图显示成功界面 或 跳转
                //视图的功能主要是  显示 点击反馈 跳转
                UserCenter.getInstance().setUserShareP(result);
                AppData.token = result.getToken();
                mView.showSuccessResult();
                // 调用 JPush 接口来设置别名。
                JPushInterface.setAlias(mView.getContext(),1,result.getCustomer().getPhone());

                //return true 不让父类内部消费
                //return false 父类会消费此事件
                return super.OnSucceed(result);
            }

            //失败
            @Override
            protected boolean onFailure(String message) {
                //与OnSucceed 一样
                return super.onFailure(message);
            }
        });
    }

    @Override
    public void destroy() {
        combineDisposable.dispose();
    }
}
