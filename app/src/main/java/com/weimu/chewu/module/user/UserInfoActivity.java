package com.weimu.chewu.module.user;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.module.id_code.IdCodeContract;
import com.weimu.chewu.module.id_code.IdCodePresenterImpl;
import com.weimu.chewu.module.register.DriverAuthenticationActivity;
import com.weimu.chewu.module.user.phone.ModifyPhoneCodeActivity;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.library.ImagePicker;
import com.weimu.library.view.ImageSelectorActivity;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

public class UserInfoActivity extends BaseActivity implements IdCodeContract.mView {
    @BindView(R.id.userinfo_civ_head)
    CircleImageView civ_head;
    @BindView(R.id.userinfo_tv_name)
    TextView tv_name;
    @BindView(R.id.userinfo_tv_phone)
    TextView tv_phone;

    private IdCodeContract.mPresenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_user_info;
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

    @Override
    protected void onResume() {
        super.onResume();
        initUserInfo();
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("个人资料");
    }

    private void initUserInfo() {
        UserB userB = UserCenter.getInstance().getUserShareP();
        if (userB != null && userB.getCustomer().getAvatar() != null) {
            Glide.with(this).load(userB.getCustomer().getAvatar()).into(civ_head);
        } else {
            Glide.with(this).load(R.drawable.head_default_64).into(civ_head);
        }
        tv_name.setText(userB.getCustomer().getName());
        tv_phone.setText(userB.getCustomer().getPhone());
    }

    @OnClick({R.id.userinfo_rl_head, R.id.userinfo_rl_phone, R.id.userinfo_rl_renzheng})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.userinfo_rl_head://头像点击
                intent = new Intent(UserInfoActivity.this, UserHeadActivity.class);
                startActivity(intent);
                break;
            case R.id.userinfo_rl_phone://手机号点击
                modifyPhoneDialog();
//                intent  = new Intent(UserInfoActivity.this,Mdify.class)
                break;
            case R.id.userinfo_rl_renzheng://司机认证点击
                intent = new Intent(UserInfoActivity.this, DriverAuthenticationActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
        }
    }

    /**
     * 修改手机号弹窗
     */
    private void modifyPhoneDialog() {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
//        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_modify_phone_notify, null);
//        //初始化控件
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
//        //设置Dialog从窗体从中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
//        dialog.setCancelable(true);
        dialog.show();//显示对话框

        TextView tv_concel = inflate.findViewById(R.id.dialog_tv_cancel);
        TextView tv_confirm = inflate.findViewById(R.id.dialog_tv_confirm);
        tv_concel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserB userB = UserCenter.getInstance().getUserShareP();
                mPresenter.getIdCode(userB.getCustomer().getPhone());
                dialog.dismiss();
            }
        });


    }

    @Override
    public void getCodeSuccess() {
        Intent intent = new Intent(UserInfoActivity.this, ModifyPhoneCodeActivity.class);
        startActivity(intent);

    }

    @Override
    public void checkIdCodeSuccess() {

    }
}
