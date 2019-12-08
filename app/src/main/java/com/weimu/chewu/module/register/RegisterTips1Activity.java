package com.weimu.chewu.module.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weimu.chewu.R;
import com.weimu.chewu.module.id_code.IdCodeContract;
import com.weimu.chewu.module.id_code.IdCodePresenterImpl;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterTips1Activity extends BaseActivity implements IdCodeContract.mView {
    @BindView(R.id.register_btn_next)
    Button btn_next;

    @BindView(R.id.register_et_phone)
    EditText et_phone;
    private String phone;
    private IdCodeContract.mPresenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_register_tips1;
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
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("注册");
    }

    @OnClick(R.id.register_btn_next)
    public void toNext() {
        phone = et_phone.getText().toString();
        if (phone.equals("")) {
            WMToast.show("请输入手机号");
            return;
        }
        if (phone.length() != 11) {
            WMToast.show("手机号不符合要求，请重新输入");
            return;
        }
        mPresenter.getIdCode(phone);

    }


    @Override
    public void getCodeSuccess() {
        Intent intent = new Intent(RegisterTips1Activity.this, RegisterTips2Activity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }

    @Override
    public void checkIdCodeSuccess() {

    }
}
