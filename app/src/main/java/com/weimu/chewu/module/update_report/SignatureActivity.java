package com.weimu.chewu.module.update_report;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.weimu.chewu.Const;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.bean.QiNiuTokenB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.http.observer.OnSingleRequestObserver;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.MainCase;
import com.weimu.chewu.backend.remote.OrderDetailCase;
import com.weimu.chewu.module.order_detail_ing.OrderDetailCaseImpl;
import com.weimu.chewu.origin.mvp.BaseView;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.utils.QiNiuUtils;
import com.weimu.chewu.widget.LinePathView;
import com.weimu.chewu.widget.ToolBarManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * 签名手写板
 */
public class SignatureActivity extends BaseActivity implements BaseView {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_signature;
    }

    @BindView(R.id.linePathView)
    LinePathView linePathView;


    public static Intent newIntent(Context context, OrderItemB orderItemB) {
        Intent intent = new Intent(context, SignatureActivity.class);
        intent.putExtra("orderItem", orderItemB);
        return intent;
    }

    private OrderDetailCase mCase;
    private OrderItemB orderItemB;
    private QiNiuTokenB qiNiuToken;

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        orderItemB = (OrderItemB) getIntent().getSerializableExtra("orderItem");
        mCase = new OrderDetailCaseImpl();
        getToken();
    }

    /**
     * 检查权限
     */
    private void checkPermissionToPickImage() {
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            saveSignature();
                        }
                    }
                });
    }

    private String filePath = Const.FILE_APP_HOME + "signature.png";

    private void saveSignature() throws IOException {
        if (linePathView.getTouched()) {
            linePathView.save(filePath, true, 10);
            compressImage(filePath);
        } else {
            showToast("您没有签名");
        }
    }


    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initToolBar();
    }

    //压缩
    private void compressImage(final String url) {

        Flowable.just(url)
                .observeOn(Schedulers.io())
                .map(new Function<String, File>() {
                    @Override
                    public File apply(String strings) throws Exception {
                        return Luban.with(getContext()).load(strings).get().get(0);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        updateImages(file);
                    }
                });


    }

    private void updateImages(final File file) {
        Logger.e("文件地址=" + file.toString());
        QiNiuUtils.getInstance().initConfig().upLoad(file, qiNiuToken.getToken(), new QiNiuUtils.OnCompleteListener() {

            @Override
            public void OnCompleteCallBack(String key, ResponseInfo info, JSONObject response) {

                String url = qiNiuToken.getBase_url() + "/" + key;
                finishOrder(url);
                Logger.e("返回的地址=" + url);
            }

            @Override
            public void OnProgress(String key, double percent) {

            }
        });
    }


    private void finishOrder(String electron_sign_img) {
        mCase.finishOrder(orderItemB.getId(), electron_sign_img).subscribe(new OnRequestObserver<String>(this) {
            @Override
            protected boolean OnSucceed(String result) {
                setResult(RESULT_OK);
                onBackPressed();
                return super.OnSucceed(result);
            }
        });

    }


    //获取七牛token
    private void getToken() {
        RetrofitClient.getDefault()
                .create(MainCase.class)
                .getQiNiuToken()
                .compose(RxSchedulers.<QiNiuTokenB>toMain())
                .subscribe(new OnSingleRequestObserver<QiNiuTokenB>() {
                    @Override
                    protected void onSuccess(QiNiuTokenB result) {
                        super.onSuccess(result);
                        qiNiuToken = result;
                    }
                });
    }


    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setTitle("添加手写签名")
                .setNavigationIcon(R.drawable.back)
                .setRightMenuText("确定")
                .setRightMenuTextColor(R.color.white)
                .setRightMenuTextClick(new ToolBarManager.OnMenuTextClickListener() {
                    @Override
                    public void onMenuTextClick() {
                        //todo 确定按钮
                        checkPermissionToPickImage();
                    }
                });
    }


}
