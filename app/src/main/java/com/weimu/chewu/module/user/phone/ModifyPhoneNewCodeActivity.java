package com.weimu.chewu.module.user.phone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weimu.chewu.AppData;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.ModifyPhoneCase;
import com.weimu.chewu.module.id_code.IdCodeContract;
import com.weimu.chewu.module.id_code.IdCodePresenterImpl;
import com.weimu.chewu.module.loginx.LoginActivity;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class ModifyPhoneNewCodeActivity extends BaseActivity implements IdCodeContract.mView {
    @BindView(R.id.modify_phone_tv_notify)
    TextView tv_notify;
    @BindView(R.id.modify_phone_et_code)
    EditText et_code;

    @BindView(R.id.modify_phone_btn_next)
    Button btn_next;
    private String phone;
    private IdCodeContract.mPresenter mPresenter;
    private String code;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_modify_phone_new_code;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        getData();
        mPresenter = new IdCodePresenterImpl(this);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        fillData();
    }

    private void getData() {
        phone = getIntent().getStringExtra("phone");
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setRightMenuIconRes(R.drawable.more_1)
                .setTitle("新手机号");
    }

    private void fillData() {
        UserB userB = UserCenter.getInstance().getUserShareP();
        tv_notify.setText("已向" + phone + "发送一个验证码");
    }

    @OnClick(R.id.modify_phone_btn_next)
    public void onClick() {
        code = et_code.getText().toString();
        if (code.equals("")) {
            Log.e("code", code);
            WMToast.show("请输入验证码");
            return;
        }
        mPresenter.checkIdCode(phone, code);

    }

    @Override
    public void getCodeSuccess() {

    }

    @Override
    public void checkIdCodeSuccess() {
        RetrofitClient.getDefault()
                .create(ModifyPhoneCase.class)
                .resetPhone(phone, code)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain())
                .subscribe(new OnRequestObserver<String>() {
                    @Override
                    protected boolean OnSucceed(String result) {
                        WMToast.show("修改手机号成功，请重新登录！");
                        AppData.closeAllActivity();
                        Intent intent = new Intent(ModifyPhoneNewCodeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        UserCenter.getInstance().setUserShareP(null);
                        return super.OnSucceed(result);
                    }

                    @Override
                    protected boolean onFailure(String message) {
                        return super.onFailure(message);
                    }

                    @Override
                    protected void onStart(Disposable d) {
                        super.onStart(d);
                    }
                });

    }
}
