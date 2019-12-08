package com.weimu.chewu.module.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.weimu.chewu.AppData;
import com.weimu.chewu.R;
import com.weimu.chewu.module.login.forget_pwd.ForgetPwdContract;
import com.weimu.chewu.module.login.forget_pwd.ForgetPwdPresenterImpl;
import com.weimu.chewu.module.loginx.LoginActivity;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPwdTip3Activity extends BaseActivity implements ForgetPwdContract.View {
    @BindView(R.id.forget_pwd_et_password)
    EditText et_password;
    private String phone, code;

    private ForgetPwdContract.Presenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_forget_pwd_tip3;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new ForgetPwdPresenterImpl(this);
        getData();
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("设置新密码");
    }

    @OnClick(R.id.forget_pwd_btn_next)
    public void onClick() {
        String password = et_password.getText().toString();
        mPresenter.doResetPwd(phone, code, password);
    }

    private void getData() {
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
    }

    @Override
    public void resetSuccess() {
        WMToast.show("重置密码成功");
        AppData.closeAllActivity();
        Intent intent = new Intent(ForgetPwdTip3Activity.this, LoginActivity.class);
        startActivity(intent);
    }
}
