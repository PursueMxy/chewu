package com.weimu.chewu.module.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.weimu.chewu.R;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterTips3Activity extends BaseActivity {
    @BindView(R.id.register_et_password)
    EditText et_password;
    @BindView(R.id.register_btn_next)
    Button btn_next;
    private String phone, code;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_register_tips3;
    }


    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        getData();
    }

    private void getData() {
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
    }
    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("注册");
    }


    @OnClick(R.id.register_btn_next)
    public void toNext() {
        String password = et_password.getText().toString();
        if (password.equals("")) {
            WMToast.show("请输入验证码");
            return;
        }
        if (password.length() < 6) {
            WMToast.show("密码不能小于6位");
            return;
        }
        Intent intent = new Intent(RegisterTips3Activity.this, RegisterTips4Activity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("code", code);
        intent.putExtra("password", password);
        startActivity(intent);
    }
}
