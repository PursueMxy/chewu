package com.weimu.chewu.module.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.module.id_code.IdCodeContract;
import com.weimu.chewu.module.id_code.IdCodePresenterImpl;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPwdTip2Activity extends BaseActivity implements IdCodeContract.mView {
    private String phone;
    @BindView(R.id.forget_pwd_et_code)
    EditText et_code;
    @BindView(R.id.forget_pwd_tv_notify)
    TextView tv_notify;
    private IdCodeContract.mPresenter mPresenter;
    private String code;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_forget_pwd_tip2;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        phone = getIntent().getStringExtra("phone");
        mPresenter = new IdCodePresenterImpl(this);
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
        tv_notify.setText("已向" + phone + "发送一个验证码");
    }

    @OnClick(R.id.forget_pwd_btn_next)
    public void onClick() {
        code = et_code.getText().toString();
//        if (!code.equals("999999")) {
//            WMToast.show("验证码不正确，请重新输入！");
//            return;
//        }
        if (code.equals("")) {
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
        Intent intent = new Intent(ForgetPwdTip2Activity.this, ForgetPwdTip3Activity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("code", code);
        startActivity(intent);
    }
}
