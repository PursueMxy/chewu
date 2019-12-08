package com.weimu.chewu.module.order_detail_arrival;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.AppSharePreB;
import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.bean.QiNiuTokenB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.observer.OnSingleRequestObserver;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.AddArrivalPicCase;
import com.weimu.chewu.backend.remote.MainCase;
import com.weimu.chewu.module.order_detail_arrival.adapter.ImageGridadapter;
import com.weimu.chewu.origin.center.SharePreferenceCenter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.utils.QiNiuUtils;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMProgressBar;
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
import io.reactivex.functions.Consumer;

/**
 * 增加到达图片
 */
public class AddArrivalPictureActivity extends BaseActivity implements AddArrivalPicContract.View {


    private AddArrivalPicCase mCase;
    private AddArrivalPicContract.Presenter mPresernter;
    @BindView(R.id.iv_huahen_checked)
    ImageView iv_huahen;
    private int order_id;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_add_arrival_picture;
    }


    public static Intent newIntent(Context context, OrderItemB orderItemB) {
        Intent intent = new Intent(context, AddArrivalPictureActivity.class);
        intent.putExtra("orderItem", orderItemB);
        return intent;
    }

    private OrderItemB orderItemB;
    public QiNiuTokenB qiNiuToken;
    private int currentImageId = -1;
    private List<String> updateUrls = new ArrayList<>();
    private List<String> updateHuaHenUrls = new ArrayList<>();
    public String[] updateOtherUrls = new String[5];
    private RecyclerView recyclerView;
    private ImageGridadapter mAdapter;
    private GridLayoutManager gridManager;
    private int maxImageNumber = 9;
    private int spanCount = 4;
    private boolean isHuaHenImage;
    private int clickCurrentItem;
    private boolean isCheckHuahen;
    private String upOhterUrl_1, upOhterUrl_2, upOhterUrl_3, upOhterUrl_4, upOhterUrl_5;

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        orderItemB = (OrderItemB) getIntent().getSerializableExtra("orderItem");
        order_id = orderItemB.getId();
        mCase = new AddArrivalPicCaseImpl();
        mPresernter = new AddArrivalPicPresenterImpl(this);
        getToken();
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        initRecyclerVIew();
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setTitle("照片上传")
                .setNavigationIcon(R.drawable.back);
    }

    @OnClick(R.id.iv_huahen_checked)
    public void onClick() {
        if (isCheckHuahen) {
            isCheckHuahen = false;
            iv_huahen.setImageResource(R.drawable.order_detail_scratch_d);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            isCheckHuahen = true;
            iv_huahen.setImageResource(R.drawable.order_detail_scratch_p);
            recyclerView.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.iv_image_1_pick, R.id.iv_image_3_pick, R.id.iv_image_4_pick, R.id.iv_image_4_pick_2, R.id.iv_image_5_pick})
    public void clickToPick(View view) {
        switch (view.getId()) {
            case R.id.iv_image_1_pick:
                clickCurrentItem = 0;
                break;
            case R.id.iv_image_3_pick:
                clickCurrentItem = 1;
                break;
            case R.id.iv_image_4_pick:
                clickCurrentItem = 2;
                break;
            case R.id.iv_image_4_pick_2:
                clickCurrentItem = 3;
                break;
            case R.id.iv_image_5_pick:
                clickCurrentItem = 4;
                break;
        }
        currentImageId = view.getId();
        isHuaHenImage = false;
        checkPermissionToPickImage();
    }


    /**
     * 检查权限并跳转到拍照页面
     */
    private void checkPermissionToPickImage() {
        new RxPermissions(this).request(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ImagePicker.getInstance().takePhoto(AddArrivalPictureActivity.this);
//                            ImagePicker.getInstance().pickImage(AddArrivalPictureActivity.this, 1);
                        }
                    }
                });
    }


    //多图上传
    private void updateImages(final File file, final int imageID) {
        QiNiuUtils.getInstance().initConfig().upLoad(file, qiNiuToken.getToken(), new QiNiuUtils.OnCompleteListener() {
            @Override
            public void OnCompleteCallBack(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    String url = qiNiuToken.getBase_url() + "/" + key;
                    if (!isHuaHenImage) {
                        Glide.with(AddArrivalPictureActivity.this)
                                .load(url).apply(new RequestOptions().centerCrop())
                                .into((ImageView) findViewById(imageID));
                        switch (clickCurrentItem) {
                            case 0:
                                upOhterUrl_1 = url;
                                break;
                            case 1:
                                upOhterUrl_2 = url;
                                break;
                            case 2:
                                upOhterUrl_3 = url;
                                break;
                            case 3:
                                upOhterUrl_4 = url;
                                break;
                            case 4:
                                upOhterUrl_5 = url;
                                break;
                        }
//                        updateOtherUrls[clickCurrentItem] = url;
                    } else {
                        updateHuaHenUrls.add(url);
                        mAdapter.setDataList(updateHuaHenUrls);
                        mAdapter.notifyDataSetChanged();
                    }
                    WMProgressBar.hideProgressDialog();
                    Logger.e("返回的地址=" + url);
                } else {
                    WMToast.show("上传失败");
                }

            }

            @Override
            public void OnProgress(String key, double percent) {
                Logger.e("进度=" + percent);
                String perStr = percent + "";
//                    progressBar.setContent("上传进度:" + (int) (percent * 100) + "%");
                WMProgressBar.showProgressDialog(getContext(), "上传进度：" + (int) (percent * 100) + "%");

                if (perStr.equals("1.0")) {
                    Logger.e("进度100=" + percent);
                    WMProgressBar.hideProgressDialog();
                }
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
//        for (int i = 0; i < 5; i++) {
//            if (TextUtils.isEmpty(updateOtherUrls[i])) {
//                showToast("照片必须都上传");
//                return;
//            } else {
//                Log.e("xxxxxxx", updateOtherUrls[i]);
//            }
//        }
        if (TextUtils.isEmpty(upOhterUrl_1) || TextUtils.isEmpty(upOhterUrl_2) || TextUtils.isEmpty(upOhterUrl_3) || TextUtils.isEmpty(upOhterUrl_4) || TextUtils.isEmpty(upOhterUrl_5)) {
            showToast("照片必须都上传");
            return;
        } else {
            updateOtherUrls[0] = upOhterUrl_1;
            updateOtherUrls[1] = upOhterUrl_2;
            updateOtherUrls[2] = upOhterUrl_3;
            updateOtherUrls[3] = upOhterUrl_4;
            updateOtherUrls[4] = upOhterUrl_5;
        }

        updateUrls.clear();
        for (int i = 0; i < 5; i++) {
            updateUrls.add(updateOtherUrls[i]);
        }
        if (!isCheckHuahen) {
            updateUrls.addAll(updateHuaHenUrls);
        }
        String car_images = new Gson().toJson(updateUrls);
        mPresernter.upLoadPic(orderItemB.getId(), car_images);
    }


    private void initRecyclerVIew() {
        recyclerView = (RecyclerView) findViewById(R.id.id_RecyclerView);
        mAdapter = new ImageGridadapter(this, maxImageNumber);
        gridManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(gridManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, WindowsUtils.dip2px(8), false));
        //设置Item增加、移除动画
        //recyclerView.setItemAnimator(new NoAlphaItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mAdapter.AddListenter(new ImageGridadapter.AddListenter() {
            @Override
            public void onAddClick(int needNumber) {
                if (updateHuaHenUrls.size() == 9) {
                    WMToast.show("最多只能传9张图片");
                    return;
                }
                isHuaHenImage = true;
                ImagePicker.getInstance().takePhoto(AddArrivalPictureActivity.this);
                //ImagePicker.getInstance().pickImage(AddArrivalPictureActivity.this, 9 - updateHuaHenUrls.size());

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", requestCode + "");

        if (resultCode != RESULT_OK) {
            return;
        }
//        Log.e("requestCode",requestCode+"");
        if (requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
//            showProgressBar();
//            WMProgressBar.showProgressDialog(this,"上传进度：0%");
            final ArrayList<String> pics = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            final ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < pics.size(); i++) {
                files.add(new File(pics.get(i)));
                Log.e("pics", pics.get(i));
            }
            if (isHuaHenImage) {
                updateImages(new File(pics.get(0)), -1);
            } else {
                updateImages(new File(pics.get(0)), currentImageId);
            }
        }
    }


    @Override
    public void onUploadPicSuccess() {
        //图片上传成功后，复制orderid
        SharePreferenceCenter center = SharePreferenceCenter.getInstance();
        AppSharePreB appShareP = center.getAppShareP();
        appShareP.setOrderId(order_id);
        appShareP.setUploadPosition(null);
        center.setAppShareP(appShareP);
        startActivity(OrderDetailArrivalActivity.newIntent(getContext(), orderItemB, (ArrayList<String>) updateUrls));
        setResult(RESULT_OK);
        onBackPressed();
    }

    @Override
    public void onUploadPicFail(String message) {

    }
}
