package com.weimu.chewu.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.weimu.chewu.R;
import com.weimu.chewu.module.id_code.IdCodeContract;
import com.weimu.chewu.module.id_code.IdCodePresenterImpl;
import com.weimu.chewu.module.login.check_register.CheckRegisterContract;
import com.weimu.chewu.module.login.check_register.CheckRegisterPresenterImpl;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPwdTip1Activity extends BaseActivity implements CheckRegisterContract.View, IdCodeContract.mView {
    @BindView(R.id.forget_pwd_et_phone)
    EditText et_phone;

    private String phone;
    private CheckRegisterContract.Presenter mPresenter;
    private IdCodeContract.mPresenter codePresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_forget_pwd_tip1;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new CheckRegisterPresenterImpl(this);
        codePresenter = new IdCodePresenterImpl(this);

    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("忘记密码");
    }

    @OnClick(R.id.forget_pwd_btn_next)
    public void onClick() {
        phone = et_phone.getText().toString();
        mPresenter.doCheck(phone);
    }

    @Override
    public void checkSuccess(int status) {
        if (status == 1) {
            codePresenter.getIdCode(phone);

        } else {
            WMToast.show("该手机号尚未注册");
        }

    }

    @Override
    public void getCodeSuccess() {
        Intent intent = new Intent(ForgetPwdTip1Activity.this, ForgetPwdTip2Activity.class);
        intent.putExtra("phone", et_phone.getText().toString());
        startActivity(intent);
    }

    @Override
    public void checkIdCodeSuccess() {

    }
}
