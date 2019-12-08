package com.weimu.chewu.module.loginx;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.weimu.chewu.R;
import com.weimu.chewu.module.login.ForgetPwdTip1Activity;
import com.weimu.chewu.module.main.MainActivity;
import com.weimu.chewu.module.register.RegisterTips1Activity;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;
import com.weimu.universalib.utils.AuthUtils;
import com.weimu.universalib.utils.EditTextUtils;
import com.weimu.universalib.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

//登录界面
public class LoginActivity extends BaseActivity implements LoginContract.View {
    @BindView(R.id.login_iv_pwd_see)
    ImageView iv_see;
    private boolean isSeePwd = false;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    private LoginContract.Presenter mPresenter;


    @BindView(R.id.login_et_phone)
    EditText etPhone;

    @BindView(R.id.login_et_pwd)
    EditText etPwd;


    //视图加载前  初始化数据
    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new LoginPresenterImpl(this);
    }

    //视图加载后 操作视图
    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
//                .setNavigationIcon(R.drawable.back)
                .setRightMenuTextColor(R.color.white)
                .setRightMenuTextSize(16)
                .setRightMenuText("注册")
                .setTitle("登录");
        ToolBarManager.with(this, getContentView()).setRightMenuTextClick(new ToolBarManager.OnMenuTextClickListener() {
            @Override
            public void onMenuTextClick() {
                Intent intent = new Intent(LoginActivity.this, RegisterTips1Activity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void showSuccessResult() {
        //登录成功 跳转到主界面
        startActivity(MainActivity.newInstance(getContext()));
        onBackPressed();
    }


    @OnClick({R.id.login_btn_login, R.id.login_tv_forget, R.id.login_rl_pwd_see})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login://登录
                String phone = EditTextUtils.getContent(etPhone);
                String pwd = EditTextUtils.getContent(etPwd);
                //视图拿到数据 转交给p来处理
                String info = checkAllValues(phone, pwd);
                if (info != null) {
                    WMToast.show(info);
                    return;
                }
                mPresenter.login(phone, pwd);
                break;
            case R.id.login_tv_forget://忘记密码
                Intent intent = new Intent(LoginActivity.this, ForgetPwdTip1Activity.class);
                startActivity(intent);
                break;
            case R.id.login_rl_pwd_see://密码显示
                if (isSeePwd) {
                    isSeePwd = false;
                    iv_see.setImageResource(R.drawable.login_pwd_no_see);
                    etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isSeePwd = true;
                    iv_see.setImageResource(R.drawable.login_see);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;

        }

    }

    //检查所有编辑框输入的值
    private String checkAllValues(String account, String pwd) {
        if (TextUtils.isEmpty(account)) {
            return "账号/手机号为空";
        }
        if (TextUtils.isEmpty(pwd)) {
            return "密码输入为空";
        }
        if (!AuthUtils.isMobileNo(account)) {
            return "请确保手机号格式输入正确";
        }
        if (!StringUtils.isValidateOr(pwd)) {
            return "请确保密码是6~20位字母或数字的组成";
        }
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
