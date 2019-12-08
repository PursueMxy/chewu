package com.weimu.chewu.module.register;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class RegisterTips4Activity extends BaseActivity {
    @BindView(R.id.register_et_real_name)
    EditText et_real_name;
    @BindView(R.id.register_et_passport)
    EditText et_passport;
    @BindView(R.id.driver_authentication_iv_id_card_zheng)
    ImageView iv_id_card_zheng;
    @BindView(R.id.driver_authentication_iv_id_card_fan)
    ImageView iv_id_card_fan;
    private int imageType;//1身份证正面，2身份证反面
    private String[] loadupImages = new String[6];
    private String id_zheng, id_fan;
    //权限声明
    RxPermissions rxPermissions;
    public QiNiuTokenB qiNiuToken;
    private String phone, code, password;
    private String realName = "", passport = "";
    private MaterialDialog progressBar;

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
        return R.layout.activity_register_tips4;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        rxPermissions = new RxPermissions(this);
        getData();
        getToken();
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
    }


    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("司机认证");
    }


    @OnClick({R.id.driver_authentication_iv_id_card_zheng, R.id.driver_authentication_iv_id_card_fan, R.id.register_btn_next})
    public void idCardZhengClick(View view) {
        switch (view.getId()) {
            case R.id.driver_authentication_iv_id_card_zheng://身份证正面拍照点击
                imageType = 1;
                checkPermissonToCamera();
                break;
            case R.id.driver_authentication_iv_id_card_fan://身份证反面拍照点击
                imageType = 2;
                checkPermissonToCamera();
                break;
            case R.id.register_btn_next://下一步
                doNext();
                break;

        }

    }

    private void doNext() {
        realName = et_real_name.getText().toString();
        passport = et_passport.getText().toString();
        if (realName.equals("")) {
            WMToast.show("请输入真实姓名");
            return;
        }
        if (passport.equals("")) {
            WMToast.show("请输入身份证号");
            return;
        }
        if (passport.length() != 18) {
            WMToast.show("身份证号必须18位");
            return;
        }
        if (TextUtils.isEmpty(id_fan)) {
            WMToast.show("请拍摄身份证反面");
            return;
        }
        if (TextUtils.isEmpty(id_zheng)) {
            WMToast.show("请拍摄身份证正面");
            return;
        }
        Intent intent = new Intent(RegisterTips4Activity.this, RegisterTips5Activity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        intent.putExtra("code", code);
        intent.putExtra("name", realName);
        intent.putExtra("passport", passport);
        intent.putExtra("id_zheng", id_zheng);
        intent.putExtra("id_fan", id_fan);
        startActivity(intent);

    }

    private void getData() {
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
        password = getIntent().getStringExtra("password");
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
                            ImagePicker.getInstance().pickImage(RegisterTips4Activity.this, 1, true);
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
            Log.e("imageview",images.get(0)+"条");
            showProgressBar();
            QiNiuUtils.getInstance().initConfig().upLoad(file, qiNiuToken.getToken(), new QiNiuUtils.OnCompleteListener() {
                @Override
                public void OnCompleteCallBack(String key, ResponseInfo info, JSONObject response) {
                    progressBar.dismiss();
                    if (info.isOK()){
                        switch (imageType) {
                            case 1://身份证正面
                                id_zheng = qiNiuToken.getBase_url() + "/" + key;
//                            Log.e("ttt", loadupImages[0]);
                                Glide.with(RegisterTips4Activity.this).load(id_zheng).into(iv_id_card_zheng);
                                break;
                            case 2://身份证反面
                                id_fan = qiNiuToken.getBase_url() + "/" + key;
                                Glide.with(RegisterTips4Activity.this).load(id_fan).into(iv_id_card_fan);
                                break;
                        }
                    }else {
//                        Log.e(())
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
