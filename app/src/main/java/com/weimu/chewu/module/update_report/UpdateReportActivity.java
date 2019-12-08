package com.weimu.chewu.module.update_report;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.bean.QiNiuTokenB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.http.observer.OnSingleRequestObserver;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.MainCase;
import com.weimu.chewu.backend.remote.OrderDetailCase;
import com.weimu.chewu.module.order_detail_arrival.adapter.ImageGridadapter;
import com.weimu.chewu.module.order_detail_ing.OrderDetailCaseImpl;
import com.weimu.chewu.origin.mvp.BaseView;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.utils.QiNiuUtils;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;
import com.weimu.library.ImagePicker;
import com.weimu.library.utils.GridSpacingItemDecoration;
import com.weimu.library.view.ImageSelectorActivity;
import com.weimu.universalib.utils.WindowsUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * 上传检测报告
 */
public class UpdateReportActivity extends BaseActivity implements BaseView {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_update_report;
    }

    public static Intent newIntent(Context context, OrderItemB orderItemB) {
        Intent intent = new Intent(context, UpdateReportActivity.class);
        intent.putExtra("orderItem", orderItemB);
        return intent;
    }


    private OrderItemB orderItemB;
    private OrderDetailCase mCase;


    public QiNiuTokenB qiNiuToken;

    private List<String> updateUrls = new ArrayList<>();

    @BindView(R.id.recy_images)
    RecyclerView recyclerView;


    private ImageGridadapter mAdapter;

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        orderItemB = (OrderItemB) getIntent().getSerializableExtra("orderItem");
        mCase = new OrderDetailCaseImpl();
        getToken();
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        initRecyclerVIew();
    }

    private void initRecyclerVIew() {
        mAdapter = new ImageGridadapter(this, 6);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, WindowsUtils.dip2px(8), false));
        //设置Item增加、移除动画
        //recyclerView.setItemAnimator(new NoAlphaItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mAdapter.AddListenter(new ImageGridadapter.AddListenter() {
            @Override
            public void onAddClick(int needNumber) {
                if (updateUrls.size() == 9) {
                    WMToast.show("最多只能传9张图片");
                    return;
                }
                new RxPermissions(UpdateReportActivity.this).request(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    ImagePicker.getInstance().pickImage(UpdateReportActivity.this, 6 - updateUrls.size());
                                } else {
                                    showToast("您没有拍照权限，请开启");
                                }
                            }
                        });


            }

            @Override
            public void onItemClick(int position) {
//                startActivity(PhotoViewPagerActivity.newInstance(MainActivity.this, position, (ArrayList<String>) mAdapter.getDataList()));
            }

            @Override
            public void onItemDeleteClick(int position) {
                mAdapter.deleteData(position);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            currentIndex = 0;
            uploadImages(images);
        }
    }

    private int currentIndex = 0;//当前指针

    private void uploadImages(final List<String> filePaths) {
        if (filePaths.isEmpty()) return;
        showProgressBar();
        if (currentIndex > filePaths.size() - 1) {
            hideProgressBar();
            return;
        }
        String targetFilePath = filePaths.get(currentIndex);
        //开始压缩
        Flowable.just(targetFilePath)
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
                    public void accept(final File file) throws Exception {
                        //上传文件
                        QiNiuUtils.getInstance().initConfig().upLoad(file, qiNiuToken.getToken(), new QiNiuUtils.OnCompleteListener() {

                            @Override
                            public void OnCompleteCallBack(String key, ResponseInfo info, JSONObject response) {
                                String url = qiNiuToken.getBase_url() + "/" + key;
                                Logger.e("返回的地址=" + url);
                                updateUrls.add(url);
                                mAdapter.addData(url);

                                currentIndex++;
                                uploadImages(filePaths);
                            }

                            @Override
                            public void OnProgress(String key, double percent) {

                            }
                        });
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

    @OnClick(R.id.tv_confirm)
    public void clickToConfirm() {
        if (updateUrls.size() <= 0) {
            showToast("请上传检测报告");
            return;
        }
        mCase.updateCheckResult(orderItemB.getId(), "failed", new Gson().toJson(updateUrls))
                .subscribe(new OnRequestObserver<String>(this) {
                    @Override
                    protected boolean OnSucceed(String result) {
                        setResult(RESULT_OK);
                        onBackPressed();
                        return super.OnSucceed(result);
                    }
                });
    }


    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setTitle("上传检测报告")
                .setNavigationIcon(R.drawable.back);
    }
}
