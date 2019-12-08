package com.weimu.chewu.module.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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

public class RegisterTips2Activity extends BaseActivity implements IdCodeContract.mView {
    private String phone;
    @BindView(R.id.register_tv_notify)
    TextView tv_notify;
    @BindView(R.id.register_btn_next)
    Button btn_next;
    @BindView(R.id.register_et_code)
    EditText et_code;
    private IdCodeContract.mPresenter mPresenter;
    private String code;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_register_tips2;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new IdCodePresenterImpl(this);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        getPhone();

    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("注册");
    }

    /**
     * 获取电话号码
     */
    private void getPhone() {
        phone = getIntent().getStringExtra("phone");
        tv_notify.setText("已经向" + phone + "发送验证码");
    }

    @OnClick(R.id.register_btn_next)
    public void toNext() {
        code = et_code.getText().toString();
        if (code.equals("")) {
            WMToast.show("请输入验证码");
            return;
        }
//        if (!code.equals("999999")) {
//            WMToast.show("验证码错误，请重新输入");
//            return;
//        }
        mPresenter.checkIdCode(phone, code);

    }

    @Override
    public void getCodeSuccess() {

    }

    @Override
    public void checkIdCodeSuccess() {
        Intent intent = new Intent(RegisterTips2Activity.this, RegisterTips3Activity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("code", code);
        startActivity(intent);
    }
}
