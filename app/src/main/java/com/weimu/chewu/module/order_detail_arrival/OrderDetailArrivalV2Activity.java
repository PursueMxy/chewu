package com.weimu.chewu.module.order_detail_arrival;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weimu.chewu.Const;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.AppSharePreB;
import com.weimu.chewu.backend.bean.OrderDetailB;
import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.bean.PositionB;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.OrderDetailCase;
import com.weimu.chewu.dialog.ChooseCheckResultDialog;
import com.weimu.chewu.module.navi.NaviActivity;
import com.weimu.chewu.module.navi.PathPlanActivity;
import com.weimu.chewu.module.order_detail_arrival.adapter.ImagesAdapter;
import com.weimu.chewu.module.order_detail_ing.AddRemarkActivity;
import com.weimu.chewu.module.order_detail_ing.OrderDetailCaseImpl;
import com.weimu.chewu.module.order_detail_ing.adapter.MessageAdapter;
import com.weimu.chewu.module.pathtrack.PathTrackActivity;
import com.weimu.chewu.module.update_report.SignatureActivity;
import com.weimu.chewu.module.update_report.UpdateReportActivity;
import com.weimu.chewu.origin.center.SharePreferenceCenter;
import com.weimu.chewu.origin.mvp.BaseView;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.type.MyNaviType;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;
import com.weimu.universalib.origin.view.list.decoration.GridSpacingItemDecoration;
import com.weimu.universalib.utils.IntentUtils;
import com.weimu.universalib.utils.SpannableUtils;
import com.weimu.universalib.utils.WindowsUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单详情  已经选择监测站
 */
public class OrderDetailArrivalV2Activity extends BaseActivity implements BaseView {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_order_detail_arrival_v2;
    }


    public static Intent newIntent(Context context, OrderItemB orderItemB) {
        Intent intent = new Intent(context, OrderDetailArrivalV2Activity.class);
        intent.putExtra("orderItem", orderItemB);
        return intent;
    }

    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_order_type)
    TextView TvOrderType;
    @BindView(R.id.tv_order_card_no)
    TextView tvOrderCarNo;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_order_location)
    TextView tvOrderLocation;
    @BindView(R.id.tv_order_fee)
    TextView tvOrderFee;
    @BindView(R.id.tv_owner_name)
    TextView tvOwnerName;
    @BindView(R.id.tv_owner_phone)
    TextView tvOwnerPhone;

    @BindView(R.id.cl_message)
    ConstraintLayout clMessage;
    @BindView(R.id.recyclerview_message)
    RecyclerView recyclerViewMessage;
    @BindView(R.id.recyclerview_images)
    RecyclerView recyclerViewImage;

    //监测站
    @BindView(R.id.tv_check_station)
    TextView tvCheckStation;
    @BindView(R.id.tv_check_result)
    TextView tvCheckResult;

    //检测报告
    @BindView(R.id.tv_check_report)
    TextView tvReport;
    @BindView(R.id.recyclerview_reports)
    RecyclerView recyReports;

    //电子签名
    @BindView(R.id.cl_signature)
    View viewSignature;
    @BindView(R.id.iv_signature)
    ImageView ivSignature;
    @BindView(R.id.tv_finish_time)
    TextView tvFinishTime;


    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    private OrderItemB orderItemB;


    private OrderDetailCase mCase;
    private MessageAdapter mAdapter;
    private ImagesAdapter imageAdapter;//上传信息
    private ImagesAdapter imageAdapterV2;//检测结果
    private OrderDetailB result;
    private int localOrderId;//缓存到本地的id

    private int currentStatus = -1;//0已经 选择监测站  1已经 上传检测结果  2 已经 签名 3还未到达监测站
    private SharePreferenceCenter center = SharePreferenceCenter.getInstance();

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        orderItemB = (OrderItemB) getIntent().getSerializableExtra("orderItem");
        localOrderId = getLocalOrderId();
        mCase = new OrderDetailCaseImpl();
    }

    private int getLocalOrderId() {
        SharePreferenceCenter center = SharePreferenceCenter.getInstance();
        AppSharePreB appShareP = center.getAppShareP();
        return appShareP.getOrderId();
    }


    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        initMessageList();
        initImageList();
        //获取订单详情
        getOrderDetail();
    }

    private int dividerWidth = 8;

    private void initImageList() {
        imageAdapter = new ImagesAdapter(getContext());
        recyclerViewImage.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewImage.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画


        recyclerViewImage.addItemDecoration(new GridSpacingItemDecoration(3, WindowsUtils.dip2px(dividerWidth), false));
        recyclerViewImage.setAdapter(imageAdapter);


        imageAdapterV2 = new ImagesAdapter(getContext());

        recyReports.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyReports.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画


        recyReports.addItemDecoration(new GridSpacingItemDecoration(3, WindowsUtils.dip2px(dividerWidth), false));
        recyReports.setAdapter(imageAdapterV2);

    }


    private void initMessageList() {
        mAdapter = new MessageAdapter(getContext());
        recyclerViewMessage.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerViewMessage.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        recyclerViewMessage.setAdapter(mAdapter);


    }

    //导航
    @OnClick(R.id.tv_navi)
    public void clickToNavi() {
        String[] locations = orderItemB.getLocation().split(",");
        PositionB end = new PositionB(Double.parseDouble(locations[1]), Double.parseDouble(locations[0]));
        end.setAddress(orderItemB.getAddress());

        startActivity(PathPlanActivity.newIntent(getContext(), orderItemB.getId(), end));
    }

    //导航到监测站
    @OnClick(R.id.tv_btn_station)
    public void clickToChooseStation() {
        WMToast.normal("导航开启中...");
        String[] locations = result.getStation().getLocation().split(",");
        PositionB end = new PositionB(Double.parseDouble(locations[1]), Double.parseDouble(locations[0]));
        end.setAddress(result.getStation().getAddress());
        //默认直接导航
        startActivity(NaviActivity.newIntent(getContext(), end, MyNaviType.DRIVE));
        //startActivity(PathPlanActivity.newIntent(getContext(), end));
    }

    //我的电话
    @OnClick(R.id.tv_owner_phone)
    public void clickToDialog() {
        IntentUtils.gotoDialog(getContext(), result.getPhone());
    }


    private void getOrderDetail() {
        mCase.getOrderList(orderItemB.getId() + "").subscribe(new OnRequestObserver<OrderDetailB>() {

            @Override
            protected boolean OnSucceed(OrderDetailB result) {
                loadDetailInfo(result);
                return super.OnSucceed(result);
            }

        });
    }

    private void loadDetailInfo(OrderDetailB result) {
        this.result = result;
        colorTextView(tvOrderNumber, "订单号：" + result.getOrder_number());
        colorTextView(TvOrderType, "代办类型：" + result.getProject());
        colorTextView(tvOrderCarNo, "车牌号：" + result.getCar_no());
        colorTextView(tvOrderTime, "接单时间：" + result.getAccepted_at() + "");
        colorTextView(tvOrderLocation, "接单地点：" + result.getAddress());
        colorTextView(tvOrderFee, "司机费用：" + result.getDriver_price() + "元");
        colorTextView(tvOwnerName, "车主姓名：" + result.getName());
        colorTextView(tvOwnerPhone, "联系电话：" + result.getPhone());
        colorTextView(tvCheckStation, "检测站：" + result.getStation().getAddress());

        List<OrderDetailB.BackupBean> backup = result.getBackup();
        if (backup == null || backup.size() == 0) {
            //backup = new ArrayList<>();
            //result.setBackup(backup);
            clMessage.setVisibility(View.GONE);
        } else {
            clMessage.setVisibility(View.VISIBLE);
        }

        mAdapter.setDataToAdapter(result.getBackup());


        showCheckResult(result);
        showCheckReport(result);
        showSignature(result);
        showConfirmBtn(result);
        loadImageListToList(result);
    }


    private void loadImageListToList(OrderDetailB result) {
        imageAdapter.setDataToAdapter(result.getCar_images());
        ViewGroup.LayoutParams layoutParams = recyclerViewImage.getLayoutParams();
        int row = ((result.getCar_images().size() - 1) / 3) + 1;
        layoutParams.height = (WindowsUtils.getScreenWidth(getContext()) - WindowsUtils.dip2px(dividerWidth * 2)) * row / 3 + WindowsUtils.dip2px((row - 1) * dividerWidth);
        recyclerViewImage.setLayoutParams(layoutParams);
    }

    private void colorTextView(TextView tv, String content) {
        tv.setText(content);
        SpannableUtils.colorNormal(tv, content.indexOf("："), content.length(), R.color.colorBlackPrimary);
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setTitle("订单详情")
                .setNavigationIcon(R.drawable.back)
                .setRightMenuText("添加备注")
                .setRightMenuTextColor(R.color.white)
                .setRightMenuTextClick(new ToolBarManager.OnMenuTextClickListener() {
                    @Override
                    public void onMenuTextClick() {
                        startActivityForResult(AddRemarkActivity.newIntent(getContext(), orderItemB), Const.INTENT_TO_ADD_REMARK);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.INTENT_TO_ADD_REMARK && resultCode == RESULT_OK) {
            getOrderDetail();
        }
        if (requestCode == Const.INTENT_TO_UPDATE_CHECK && resultCode == RESULT_OK) {
            changeToOrderFinishStatus();
        }

        if (requestCode == Const.INTENT_TO_SIGNATURE && resultCode == RESULT_OK) {
            //todo 上传路径
            changeToCheckPathStatus();
            //置空
            AppSharePreB appShareP = center.getAppShareP();
            appShareP.setOrderId(-1);
            appShareP.setUploadPosition(null);
            center.setAppShareP(appShareP);
        }
    }


    private void showCheckResult(OrderDetailB result) {
        if (!result.getCheckout_result().equals("pending")) {
            if (result.getCheckout_result().equals("success")) {
                colorTextView(tvCheckResult, "检测结果：通过");
            } else {
                colorTextView(tvCheckResult, "检测结果：未通过");
            }

        }
    }

    //显示检测报告
    private void showCheckReport(OrderDetailB result) {
        if (result.getFailed_reason_img() == null) {
            tvReport.setVisibility(View.GONE);
            recyReports.setVisibility(View.GONE);
        } else {
            if (result.getFailed_reason_img() != null && result.getFailed_reason_img().size() > 0) {
                tvReport.setVisibility(View.VISIBLE);
                recyReports.setVisibility(View.VISIBLE);
                imageAdapterV2.setDataToAdapter(result.getFailed_reason_img());
            } else {
                tvReport.setVisibility(View.GONE);
                recyReports.setVisibility(View.GONE);
            }

        }

    }

    //显示签名
    private void showSignature(OrderDetailB result) {
        if (TextUtils.isEmpty(result.getElectron_sign_img())) {
            viewSignature.setVisibility(View.GONE);
        } else {
            viewSignature.setVisibility(View.VISIBLE);
            Glide.with(this).load(result.getElectron_sign_img()).into(ivSignature);
            tvFinishTime.setText(result.getFinished_at());
        }
    }

    //显示按钮的显示
    private void showConfirmBtn(OrderDetailB result) {
        if (!result.getCheckout_result().equals("pending")) {
            if (!TextUtils.isEmpty(result.getElectron_sign_img())) {
                currentStatus = 2;//
                tvConfirm.setText("查看行驶路径");
            } else {
                currentStatus = 1;
                tvConfirm.setText("完成订单");
            }

        } else {
            //如果本地订单id等于当前订单id，证明正在上传这个订单的位置
            if (localOrderId == orderItemB.getId()) {
                currentStatus = 3;
                tvConfirm.setText("到达检测站");
            } else {
                currentStatus = 0;
                tvConfirm.setText("上传检测结果");
            }

        }
    }


    //检查监测站
    @OnClick(R.id.tv_confirm)
    public void clickToConfirm() {
        SharePreferenceCenter center = SharePreferenceCenter.getInstance();
        AppSharePreB appShareP = center.getAppShareP();
        switch (currentStatus) {
            case 0://上传检测结果
                appShareP.setOrderId(-1);
                appShareP.setUploadPosition(null);
                center.setAppShareP(appShareP);
                showCheckResultDIalog();
                break;
            case 1://完成订单
                //完成订单后，关闭定位
                startActivityForResult(SignatureActivity.newIntent(getContext(), orderItemB), Const.INTENT_TO_SIGNATURE);
                break;
            case 2://查看行驶路径
                startActivity(PathTrackActivity.newIntent(getContext(), result));
                break;
            case 3://到达检测站
                currentStatus = 0;
                tvConfirm.setText("上传检测结果");
                //到达检测站后，关闭定位
                appShareP.setOrderId(-1);
                appShareP.setUploadPosition(null);
                center.setAppShareP(appShareP);
                break;

        }
    }

    private void showCheckResultDIalog() {
        ChooseCheckResultDialog dialog = (ChooseCheckResultDialog) new ChooseCheckResultDialog().show(this);
        dialog.setClickListener(new ChooseCheckResultDialog.OnClickListener() {
            @Override
            public void onClick(boolean isPass) {
//                SharePreferenceCenter center = SharePreferenceCenter.getInstance();
//                AppSharePreB appShareP = center.getAppShareP();
//                appShareP.setOrderId(-1);
//                appShareP.setUploadPosition(null);
//                center.setAppShareP(appShareP);
                if (!isPass) {
                    //没通过 上传检测结果
                    startActivityForResult(UpdateReportActivity.newIntent(getContext(), orderItemB), Const.INTENT_TO_UPDATE_CHECK);
                } else {
                    //通过直接签名
                    mCase.updateCheckResult(orderItemB.getId(), "success", null)
                            .subscribe(new OnRequestObserver<String>(OrderDetailArrivalV2Activity.this) {
                                @Override
                                protected boolean OnSucceed(String result) {
                                    changeToOrderFinishStatus();
                                    return true;
                                }
                            });
                }

            }
        });
    }

    //修改为 订单完成状态
    private void changeToOrderFinishStatus() {
        currentStatus = 1;
        tvConfirm.setText("完成订单");
        getOrderDetail();
        startActivityForResult(SignatureActivity.newIntent(getContext(), orderItemB), Const.INTENT_TO_SIGNATURE);
    }

    //修改为 查看行驶路径
    private void changeToCheckPathStatus() {
        currentStatus = 2;
        tvConfirm.setText("查看行驶路径");
        getOrderDetail();
    }


}
