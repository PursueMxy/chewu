package com.weimu.chewu.module.user.phone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.weimu.chewu.R;
import com.weimu.chewu.module.id_code.IdCodeContract;
import com.weimu.chewu.module.id_code.IdCodePresenterImpl;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyPhoneNewPhoneActivity extends BaseActivity implements IdCodeContract.mView {
    @BindView(R.id.modify_phone_et_phone)
    EditText et_phone;
    private String code;
    private String phone;
    private IdCodeContract.mPresenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_modify_phone_new_phone;
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
        getCode();
    }

    private void getCode() {
        code = getIntent().getStringExtra("code");
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("修改手机号");
    }

    @OnClick(R.id.modify_phone_btn_next)
    public void onClick() {
        phone = et_phone.getText().toString();
        if (phone.equals("")) {
            WMToast.show("请输入手机号");
            return;
        }
        mPresenter.getIdCode(phone);
    }

    @Override
    public void getCodeSuccess() {
        Intent intent = new Intent(ModifyPhoneNewPhoneActivity.this, ModifyPhoneNewCodeActivity.class);
        intent.putExtra("code", code);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }

    @Override
    public void checkIdCodeSuccess() {

    }
}
