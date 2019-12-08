package com.weimu.chewu.module.user.phone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.module.id_code.IdCodeContract;
import com.weimu.chewu.module.id_code.IdCodePresenterImpl;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyPhoneCodeActivity extends BaseActivity implements IdCodeContract.mView {
    @BindView(R.id.modify_phone_tv_notify)
    TextView tv_notify;
    @BindView(R.id.modify_phone_et_code)
    EditText et_code;

    @BindView(R.id.modify_phone_btn_next)
    Button btn_next;
    private String code;
    private IdCodeContract.mPresenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_modify_phone_code;
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
        fillData();
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setRightMenuIconRes(R.drawable.more_1)
                .setTitle("修改手机号");
    }

    private void fillData() {
        UserB userB = UserCenter.getInstance().getUserShareP();
        tv_notify.setText("已向" + userB.getCustomer().getPhone() + "发送一个验证码");
    }

    @OnClick(R.id.modify_phone_btn_next)
    public void onClick() {
        code = et_code.getText().toString();
        if (code.equals("")) {
            Log.e("code", code);
            WMToast.show("请输入验证码");
            return;
        }
        String phone = UserCenter.getInstance().getUserShareP().getCustomer().getPhone();
        mPresenter.checkIdCode(phone, code);

    }

    @Override
    public void getCodeSuccess() {

    }

    @Override
    public void checkIdCodeSuccess() {
        Intent intent = new Intent(ModifyPhoneCodeActivity.this, ModifyPhoneNewPhoneActivity.class);
        intent.putExtra("code", code);
        startActivity(intent);

    }
}
