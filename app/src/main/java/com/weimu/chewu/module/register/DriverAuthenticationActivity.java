package com.weimu.chewu.module.register;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class DriverAuthenticationActivity extends BaseActivity implements RegisterContract.View {
    @BindView(R.id.driver_authentication_et_real_name)
    EditText et_real_name;
    @BindView(R.id.driver_authentication_et_real_id_code)
    EditText et_id_code;

    @BindView(R.id.driver_authentication_iv_id_card_zheng)
    ImageView iv_id_card_zheng;
    @BindView(R.id.driver_authentication_iv_id_card_fan)
    ImageView iv_id_card_fan;
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
    @BindView(R.id.driver_authentication_tv_real_name)
    TextView tv_real_name;
    @BindView(R.id.driver_authentication_tv_real_id_code)
    TextView tv_id_code;
    private int type;

    //权限声明
    RxPermissions rxPermissions;
    public QiNiuTokenB qiNiuToken;
    private String phone, code, password;
    private int imageType;//1身份证正面，2身份证反面，3手持身份证，4驾驶证左页，5驾驶证右页，6行驶证
    private String[] loadupImages = new String[6];
    RegisterContract.Presenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_driver_authentication;
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
        type = getIntent().getIntExtra("type", -1);
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
        password = getIntent().getStringExtra("password");

        //type=1是已认证
        if (type == 1) {
            UserB userB = UserCenter.getInstance().getUserShareP();
             btn_input.setVisibility(View.INVISIBLE);
            Glide.with(this).load(userB.getCustomer().getPassport_images().get(0)).into(iv_id_card_zheng);
            Glide.with(this).load(userB.getCustomer().getPassport_images().get(1)).into(iv_id_card_fan);
            Glide.with(this).load(userB.getCustomer().getPassport_images().get(2)).into(iv_id_card_shou);
            Glide.with(this).load(userB.getCustomer().getDriver_license_images().get(0)).into(iv_driver_card_left);
            Glide.with(this).load(userB.getCustomer().getDriver_license_images().get(1)).into(iv_driver_card_right);
            Glide.with(this).load(userB.getCustomer().getDriving_license_images().get(0)).into(iv_driver_card);
            tv_real_name.setText(userB.getCustomer().getName());
            tv_id_code.setText(userB.getCustomer().getPassport());
            et_id_code.setVisibility(View.GONE);
            et_real_name.setVisibility(View.GONE);
        } else {
            tv_real_name.setVisibility(View.GONE);
            tv_id_code.setVisibility(View.GONE);
//            et_id_code.setVisibility(View.GONE);
//            et_real_name.setVisibility(View.GONE);
        }


    }

    @Override
    public void showSuccessResult() {
        WMToast.show("注册成功");
        closeAllActivity();
        startActivity(new Intent(DriverAuthenticationActivity.this, LoginActivity.class));

    }

    //身份证正面拍照点击
    @OnClick(R.id.driver_authentication_iv_id_card_zheng)
    public void idCardZhengClick() {
        if (type != 1) {
            imageType = 1;
            checkPermissonToCamera();
        }

    }


    //身份证反面拍照点击
    @OnClick(R.id.driver_authentication_iv_id_card_fan)
    public void idCardFanClick() {
        if (type != 1) {
            imageType = 2;
            checkPermissonToCamera();
        }
    }

    //手持身份证拍照点击
    @OnClick(R.id.driver_authentication_iv_id_card_shou)
    public void idCardShouClick() {
        if (type != 1) {
            imageType = 3;
            checkPermissonToCamera();
        }
    }

    //驾驶证左页拍照点击
    @OnClick(R.id.driver_authentication_iv_driver_card_left)
    public void driverCardLeftShouClick() {
        if (type != 1) {
            imageType = 4;
            checkPermissonToCamera();
        }
    }

    //驾驶证右页拍照点击
    @OnClick(R.id.driver_authentication_iv_driver_card_right)
    public void driverCardRightClick() {
        if (type != 1) {
            imageType = 5;
            checkPermissonToCamera();
        }
    }

    //行驶证拍照点击
    @OnClick(R.id.driver_authentication_iv_driver_card)
    public void driverCardClick() {
        if (type != 1) {
            imageType = 6;
            checkPermissonToCamera();
        }
    }

    @OnClick(R.id.driver_authentication_btn_input)
    public void inputClick() {
        String idCode = et_id_code.getText().toString();
        String realName = et_real_name.getText().toString();
        String[] passports = new String[3];
        passports[0] = loadupImages[0];
        passports[1] = loadupImages[1];
        passports[2] = loadupImages[2];
        String[] driverCards = new String[2];
        driverCards[0] = loadupImages[3];
        driverCards[1] = loadupImages[4];
        String[] driverCard = new String[1];
        driverCard[0] = loadupImages[5];
        StringBuffer passportBuffer = new StringBuffer();
        passportBuffer.append("[");
        for (int i = 0; i < passports.length; i++) {
            if (i == passports.length - 1) {
                passportBuffer.append("\"" + passports[i] + "\"]");
            } else {
                passportBuffer.append("\"" + passports[i] + "\",");
            }

        }

        StringBuffer driverCardsBuffer = new StringBuffer();
        driverCardsBuffer.append("[");
        for (int i = 0; i < driverCards.length; i++) {
            if (i == driverCards.length - 1) {
                driverCardsBuffer.append("\"" + driverCards[i] + "\"]");
            } else {
                driverCardsBuffer.append("\"" + driverCards[i] + "\",");
            }

        }

        StringBuffer driverCardBuffer = new StringBuffer();
        driverCardBuffer.append("[");
        for (int i = 0; i < driverCard.length; i++) {
            if (i == driverCard.length - 1) {
                driverCardBuffer.append("\"" + driverCard[i] + "\"]");
            } else {
                driverCardBuffer.append("\"" + driverCard[i] + "\",");
            }

        }
        mPresenter.register(code, phone, password, realName, idCode, passportBuffer.toString(), driverCardsBuffer.toString(), driverCardsBuffer.toString());
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
                            ImagePicker.getInstance().pickImage(DriverAuthenticationActivity.this, 1);
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
            QiNiuUtils.getInstance().initConfig().upLoad(file, qiNiuToken.getToken(), new QiNiuUtils.OnCompleteListener() {
                @Override
                public void OnCompleteCallBack(String key, ResponseInfo info, JSONObject response) {
                    switch (imageType) {
                        case 1://身份证正面
                            loadupImages[0] = qiNiuToken.getBase_url() + "/" + key;
                            Glide.with(DriverAuthenticationActivity.this).load(loadupImages[0]).into(iv_id_card_zheng);
                            break;
                        case 2://身份证反面
                            loadupImages[1] = qiNiuToken.getBase_url() + "/" + key;
                            Glide.with(DriverAuthenticationActivity.this).load(loadupImages[1]).into(iv_id_card_fan);

                            break;
                        case 3://手持身份证
                            loadupImages[2] = qiNiuToken.getBase_url() + "/" + key;
                            Glide.with(DriverAuthenticationActivity.this).load(loadupImages[2]).into(iv_id_card_shou);

                            break;
                        case 4://驾驶证左页
                            loadupImages[3] = qiNiuToken.getBase_url() + "/" + key;
                            Glide.with(DriverAuthenticationActivity.this).load(loadupImages[3]).into(iv_driver_card_left);

                            break;
                        case 5://驾驶证右页
                            loadupImages[4] = qiNiuToken.getBase_url() + "/" + key;
                            Glide.with(DriverAuthenticationActivity.this).load(loadupImages[4]).into(iv_driver_card_right);

                            break;
                        case 6://行驶证
                            loadupImages[5] = qiNiuToken.getBase_url() + "/" + key;
                            Glide.with(DriverAuthenticationActivity.this).load(loadupImages[5]).into(iv_driver_card);

                            break;

                    }
                }

                @Override
                public void OnProgress(String key, double percent) {

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
