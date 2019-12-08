package com.weimu.chewu.module.register;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
import com.weimu.chewu.module.loginx.LoginActivity;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.utils.QiNiuUtils;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;
import com.weimu.library.ImagePicker;
import com.weimu.library.view.ImageSelectorActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.weimu.universalib.OriginAppData.closeAllActivity;

public class RegisterTips5Activity extends BaseActivity implements RegisterContract.View {
    @BindView(R.id.driver_authentication_iv_id_card_shou)
    ImageView iv_id_card_shou;
    @BindView(R.id.driver_authentication_iv_driver_card_left)
    ImageView iv_driver_card_left;
    @BindView(R.id.driver_authentication_iv_driver_card_right)
    ImageView iv_driver_card_right;
    @BindView(R.id.driver_authentication_iv_driver_card)
    ImageView iv_driver_card;
    @BindView(R.id.driver_authentication_btn_input)
    Button btn_input;


    //权限声明
    RxPermissions rxPermissions;
    public QiNiuTokenB qiNiuToken;
    private String phone, code, password;
    private int imageType;//1身份证正面，2身份证反面，3手持身份证，4驾驶证左页，5驾驶证右页，6行驶证
    private String[] loadupImages = new String[6];
    RegisterContract.Presenter mPresenter;
    private String realName, passport;
    private MaterialDialog progressBar;
    private String id_zheng, id_fan;
    private String id_image_1, id_image_2, id_image_3, id_image_4;

    public void showProgressBar() {
        progressBar = new MaterialDialog.Builder(this)
                .title("正在上传中！")
                .cancelable(false)
                .content("")
                .progress(true, 0)
                .show();

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_register_tips5;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new RegisterPersenterImpl(this);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        rxPermissions = new RxPermissions(this);
        getToken();
        getData();
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("司机认证");
    }

    private void getData() {
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
        password = getIntent().getStringExtra("password");
        realName = getIntent().getStringExtra("name");
        passport = getIntent().getStringExtra("passport");
        id_zheng = getIntent().getStringExtra("id_zheng");
        id_fan = getIntent().getStringExtra("id_fan");
        loadupImages[0] = id_zheng;
        loadupImages[1] = id_fan;
    }

    @Override
    public void showSuccessResult() {
        WMToast.show("信息提交成功，等待后台审核");
        closeAllActivity();
        startActivity(new Intent(RegisterTips5Activity.this, LoginActivity.class));

    }


    //手持身份证拍照点击
    @OnClick(R.id.driver_authentication_iv_id_card_shou)
    public void idCardShouClick() {
        imageType = 3;
        checkPermissonToCamera();

    }

    //驾驶证左页拍照点击
    @OnClick(R.id.driver_authentication_iv_driver_card_left)
    public void driverCardLeftShouClick() {
        imageType = 4;
        checkPermissonToCamera();

    }

    //驾驶证右页拍照点击
    @OnClick(R.id.driver_authentication_iv_driver_card_right)
    public void driverCardRightClick() {
        imageType = 5;
        checkPermissonToCamera();

    }

    //行驶证拍照点击
    @OnClick(R.id.driver_authentication_iv_driver_card)
    public void driverCardClick() {
        imageType = 6;
        checkPermissonToCamera();

    }

    @OnClick(R.id.driver_authentication_btn_input)
    public void inputClick() {
        if (TextUtils.isEmpty(id_image_1)) {
            WMToast.show("请拍摄手持身份证照片");
            return;
        }
        if (TextUtils.isEmpty(id_image_2)) {
            WMToast.show("请拍摄驾照左页照片");
            return;
        }
        if (TextUtils.isEmpty(id_image_3)) {
            WMToast.show("请拍摄驾照右页照片");
            return;
        }
        if (TextUtils.isEmpty(id_image_4)) {
            WMToast.show("请拍摄行驶证照片");
            return;
        }
        loadupImages[2] = id_image_1;
        loadupImages[3] = id_image_2;
        loadupImages[4] = id_image_3;
        loadupImages[5] = id_image_4;
        StringBuffer passportBuffer = new StringBuffer();
        passportBuffer.append("[");
        passportBuffer.append("\"" + loadupImages[0] + "\",");
        passportBuffer.append("\"" + loadupImages[1] + "\",");
        passportBuffer.append("\"" + loadupImages[2] + "\"]");


        StringBuffer driverCardsBuffer = new StringBuffer();
        driverCardsBuffer.append("[");
        driverCardsBuffer.append("\"" + loadupImages[3] + "\",");
        driverCardsBuffer.append("\"" + loadupImages[4] + "\"]");


        StringBuffer driverCardBuffer = new StringBuffer();
        driverCardBuffer.append("[");
        driverCardBuffer.append("\"" + loadupImages[5] + "\"]");
        mPresenter.register(code, phone, password, realName, passport, passportBuffer.toString(), driverCardsBuffer.toString(), driverCardBuffer.toString());
    }

    /**
     * 检查权限并跳转到拍照页面
     */
    private void checkPermissonToCamera() {
        rxPermissions.request(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ImagePicker.getInstance().pickImage(RegisterTips5Activity.this, 1, true);
                        } else {
//                            showTipsDialog(PersonalInfoActivity.this, "没有读取权限，APP将无法正常访问照片！", "知道了", "添加权限");
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            final ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // do something
            File file = new File(images.get(0));
            showProgressBar();
            QiNiuUtils.getInstance().initConfig().upLoad(file, qiNiuToken.getToken(), new QiNiuUtils.OnCompleteListener() {
                @Override
                public void OnCompleteCallBack(String key, ResponseInfo info, JSONObject response) {
                    progressBar.dismiss();
                    if (info.isOK()){
                        switch (imageType) {
                            case 3://手持身份证
                                id_image_1 = qiNiuToken.getBase_url() + "/" + key;
                                Glide.with(RegisterTips5Activity.this).load(id_image_1).into(iv_id_card_shou);
                                break;
                            case 4://驾驶证左页
                                id_image_2 = qiNiuToken.getBase_url() + "/" + key;
                                Glide.with(RegisterTips5Activity.this).load(id_image_2).into(iv_driver_card_left);

                                break;
                            case 5://驾驶证右页
                                id_image_3 = qiNiuToken.getBase_url() + "/" + key;
                                Glide.with(RegisterTips5Activity.this).load(id_image_3).into(iv_driver_card_right);

                                break;
                            case 6://行驶证
                                id_image_4 = qiNiuToken.getBase_url() + "/" + key;
                                Glide.with(RegisterTips5Activity.this).load(id_image_4).into(iv_driver_card);
                                break;
                        }
                    }else {
                        WMToast.show("上传失败");
                    }

                }

                @Override
                public void OnProgress(String key, double percent) {
                    progressBar.setContent("上传进度:" + (int) (percent * 100) + "%");
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


}
