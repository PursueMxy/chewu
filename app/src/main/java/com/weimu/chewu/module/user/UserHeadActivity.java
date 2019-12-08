package com.weimu.chewu.module.user;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qiniu.android.http.ResponseInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.QiNiuTokenB;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.MainCase;
import com.weimu.chewu.module.loginx.LoginContract;
import com.weimu.chewu.module.user.head.UserHeadContract;
import com.weimu.chewu.module.user.head.UserHeadPresenterImpl;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.utils.IntentUtils;
import com.weimu.chewu.utils.QiNiuUtils;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMProgressBar;
import com.weimu.chewu.widget.WMToast;
import com.weimu.library.ImagePicker;
import com.weimu.library.view.ImageSelectorActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UserHeadActivity extends BaseActivity implements UserHeadContract.View {
    @BindView(R.id.userhead_iv_bg)
    ImageView iv_bg;
    @BindView(R.id.userhead_civ_head)
    CircleImageView civ_head;
    private RxPermissions rxPermissions;
    private QiNiuTokenB qiNiuToken;
    private UserHeadContract.Presenter mPresenter;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_user_head;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new UserHeadPresenterImpl(this);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        rxPermissions = new RxPermissions(this);
        initToolBar();
        initHead();
        getToken();
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("设置头像");
    }

    private void initHead() {
        UserB userB = UserCenter.getInstance().getUserShareP();
        if (UserCenter.getInstance().getUserShareP().getCustomer().getAvatar() != null) {
            Glide.with(this).load(userB.getCustomer().getAvatar()).into(iv_bg);
            Glide.with(this).load(userB.getCustomer().getAvatar()).into(civ_head);
        } else {
//            Glide.with(this).load(R.drawable.head_default_64).into(iv_bg);
            Glide.with(this).load(R.drawable.head_default_64).into(civ_head);

        }
    }

    @OnClick(R.id.userhead_civ_head)
    public void onClick() {
        choosePhotoDialog();
    }

    /**
     * 选择拍照弹窗
     */
    private void choosePhotoDialog() {
        final Dialog dialog = new Dialog(this, R.style.AlertDialogStyle);
//        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_choose_photo, null);
//        //初始化控件
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
//        //设置Dialog从窗体从中间弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialog.setCancelable(true);
        dialog.show();//显示对话框
        Display display = getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);

        TextView rl_choose = inflate.findViewById(R.id.dialog_tv_choose_photo);
        TextView rl_takePhoto = inflate.findViewById(R.id.dialog_tv_take_photo);
        TextView rl_cencel = inflate.findViewById(R.id.dialog_tv_cancel);
        //从相册中选择
        rl_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                rxPermissions.request(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    ImagePicker.getInstance().pickAvatar(UserHeadActivity.this);
//                                    ImageSelectorActivity.start(PersonalInfoActivity.this, 1, ImageSelectorActivity.MODE_SINGLE, false, false, true);
                                } else {
                                    showTipsDialog(UserHeadActivity.this, "没有读取权限，APP将无法正常访问照片！", "知道了", "添加权限");
                                }
                            }
                        });
            }
        });
        //拍照
        rl_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rxPermissions.request(android.Manifest.permission.CAMERA,android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    ImagePicker.getInstance().takePhoto(UserHeadActivity.this, true);
                                } else {
                                    showTipsDialog(UserHeadActivity.this, "没有拍照权限，APP将无法正常拍照！", "知道了", "添加权限");
                                }
                            }
                        });
                dialog.dismiss();
            }
        });
        //取消
        rl_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 权限放弃提示弹窗
     *
     * @param mContext 上下文
     * @param content  提示的内容
     * @param leftStr  左按钮文本
     * @param rightStr 右按钮文本
     * @description 通用弹窗  -提示操作
     */
    public void showTipsDialog(final Context mContext, String content, String leftStr, String rightStr) {
        //dialog
        final MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .cancelable(false)
                .backgroundColor(ContextCompat.getColor(mContext, R.color.colorTransparent))
                .customView(R.layout.dialog_universal_tip, false)
                .show();
        final View customView = dialog.getCustomView();
        //content
        TextView tvContent = (TextView) customView.findViewById(R.id.tv_content);
        tvContent.setText(content);
        //btn
        TextView tvLeft = (TextView) customView.findViewById(R.id.btn_left);
        if (TextUtils.isEmpty(leftStr)) {
            tvLeft.setVisibility(View.GONE);
        }
        tvLeft.setText(leftStr);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tvRight = (TextView) customView.findViewById(R.id.btn_right);
        tvRight.setText(rightStr);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openMyAppInfo(UserHeadActivity.this);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("111",requestCode+"");
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            final ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // do something
            File file = new File(images.get(0));
            QiNiuUtils.getInstance().initConfig().upLoad(file, qiNiuToken.getToken(), new QiNiuUtils.OnCompleteListener() {
                @Override
                public void OnCompleteCallBack(String key, ResponseInfo info, JSONObject response) {
                    mPresenter.inputUserHead(qiNiuToken.getBase_url() + "/" + key);
//                    progressBar.dismiss();
                    WMProgressBar.hideProgressDialog();
                }

                @Override
                public void OnProgress(String key, double percent) {
//                    progressBar.setContent("上传进度:" + (int) (percent * 100) + "%");
                    WMProgressBar.showProgressDialog(getContext(), "上传进度：" + (int) (percent * 100) + "%");
                }
            });
        }
    }

    //获取七牛token
    private void getToken() {
        RetrofitClient.getDefault()
                .create(MainCase.class)
                .getQiNiuToken()
                .compose(RxSchedulers.<QiNiuTokenB>toMain())
                .subscribe(new Observer<QiNiuTokenB>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(QiNiuTokenB qiNiuTokenB) {
                        qiNiuToken = qiNiuTokenB;
                        Log.e("TAG", qiNiuTokenB.getToken());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onCompete");

                    }
                });
    }

    @Override
    public void showSuccessResult(String userHead) {
//        WMToast.show("头像设置成功");
        UserB userB = UserCenter.getInstance().getUserShareP();
        userB.getCustomer().setAvatar(userHead);
        UserCenter.getInstance().setUserShareP(userB);
        initHead();
    }
}
