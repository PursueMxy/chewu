package com.weimu.chewu.module.order_detail_arrival;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.weimu.chewu.Const;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.OrderDetailB;
import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.bean.PositionB;
import com.weimu.chewu.backend.bean.StationListResultB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.OrderDetailCase;
import com.weimu.chewu.dialog.ShowPayCodeDialog;
import com.weimu.chewu.module.navi.PathPlanActivity;
import com.weimu.chewu.module.order_detail_arrival.adapter.ImagesAdapter;
import com.weimu.chewu.module.order_detail_ing.AddRemarkActivity;
import com.weimu.chewu.module.order_detail_ing.OrderDetailCaseImpl;
import com.weimu.chewu.module.order_detail_ing.adapter.MessageAdapter;
import com.weimu.chewu.module.station.StationMapActivity;
import com.weimu.chewu.origin.mvp.BaseView;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.universalib.origin.view.list.decoration.GridSpacingItemDecoration;
import com.weimu.universalib.utils.IntentUtils;
import com.weimu.universalib.utils.SpannableUtils;
import com.weimu.universalib.utils.WindowsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单详情  进行中->过来
 */
public class OrderDetailArrivalActivity extends BaseActivity implements BaseView {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_order_detail_arrival;
    }


    public static Intent newIntent(Context context, OrderItemB orderItemB, ArrayList<String> imagesList) {
        Intent intent = new Intent(context, OrderDetailArrivalActivity.class);
        intent.putExtra("orderItem", orderItemB);
        intent.putExtra("imagesList", imagesList);
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


    @BindView(R.id.tv_check_station)
    TextView tvCheckStation;
    @BindView(R.id.tv_check_result)
    TextView tvCheckResult;

    @BindView(R.id.tv_btn_station)
    TextView tvStation;

    private OrderItemB orderItemB;
    private ArrayList<String> imagesList;

    private OrderDetailCase mCase;
    private MessageAdapter mAdapter;
    private ImagesAdapter imageAdapter;
    private OrderDetailB result;

    private boolean isChooseStationId = false;//是否选择监测站


    private Timer timer;//判断账户是否付款
    private ShowPayCodeDialog showPayCodeDialog;

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        orderItemB = (OrderItemB) getIntent().getSerializableExtra("orderItem");
        imagesList = (ArrayList<String>) getIntent().getSerializableExtra("imagesList");
        mCase = new OrderDetailCaseImpl();
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        initToolBar();
        initMessageList();
        initImageList();
        //获取订单详情
        getOrderDetail();

    }

    @Override
    protected void onResume() {
        super.onResume();
        showPayDialog();
    }

    private void showPayDialog() {
        if (!orderItemB.getPayment_status().equals("approve")) {
            //todo 是否为付款订单
            showPayCodeDialog = new ShowPayCodeDialog();
            showPayCodeDialog.setUrl(orderItemB.getPayment_url());
            showPayCodeDialog.setMoney(orderItemB.getPrice() + "");
            //todo 先可以手动关闭
            showPayCodeDialog.show(this, false);
            loop();
        }
    }

    private void initImageList() {
        imageAdapter = new ImagesAdapter(getContext());
        recyclerViewImage.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewImage.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画

        int dividerWidth = 8;
        recyclerViewImage.addItemDecoration(new GridSpacingItemDecoration(3, WindowsUtils.dip2px(dividerWidth), false));
        recyclerViewImage.setAdapter(imageAdapter);

        imageAdapter.setDataToAdapter(imagesList);


        ViewGroup.LayoutParams layoutParams = recyclerViewImage.getLayoutParams();
        int row = ((imagesList.size() - 1) / 3) + 1;
        layoutParams.height = (WindowsUtils.getScreenWidth(getContext()) - WindowsUtils.dip2px(dividerWidth * 2)) * row / 3 + WindowsUtils.dip2px((row - 1) * dividerWidth);
        recyclerViewImage.setLayoutParams(layoutParams);
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

    //选择监测站
    @OnClick(R.id.tv_btn_station)
    public void clickToChooseStation() {
        if (!isChooseStationId) {
            startActivityForResult(StationMapActivity.newIntent(getContext()), Const.INTENT_TO_STATION);
        }

    }

    //上传检测结果
    @OnClick(R.id.tv_confirm)
    public void clickToConfirm() {
        showPayDialog();
        if (!isChooseStationId && orderItemB.getPayment_status().equals("approve")) {
            showToast("请选择监测站");
        }

    }

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


        List<OrderDetailB.BackupBean> backup = result.getBackup();
        if (backup == null || backup.size() == 0) {
            //backup = new ArrayList<>();
            //result.setBackup(backup);
            clMessage.setVisibility(View.GONE);
        } else {
            clMessage.setVisibility(View.VISIBLE);
        }
//
//        backup.add(new OrderDetailB.BackupBean("2018.03.13", "高雅的人,看背影就知道;奋进的人,听脚步声就知道；和善"));
//        backup.add(new OrderDetailB.BackupBean("2018.03.13", "高雅的人,看背影就知道;奋进的人,听脚步声就知道；和善"));
//        backup.add(new OrderDetailB.BackupBean("2018.03.13", "高雅的人,看背影就知道;奋进的人,听脚步声就知道；和善"));

        mAdapter.setDataToAdapter(result.getBackup());
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

        if (requestCode == Const.INTENT_TO_STATION && resultCode == RESULT_OK) {
            //onBackPressed();
            StationListResultB.StationB station = (StationListResultB.StationB) data.getSerializableExtra("station");
            colorTextView(tvCheckStation, "监测站：" + station.getName());
            receiveGuest(station.getId());
        }
    }

    //todo 建议替换成上传检测站成功后 关闭当前详情页 进入详情页2
    private void receiveGuest(int stationID) {
        String car_images = new Gson().toJson(imagesList);
        Logger.json(car_images);
        mCase.receiveGuest(orderItemB.getId(), stationID).subscribe(new OnRequestObserver<String>(this) {
            @Override
            protected boolean OnSucceed(String result) {
                isChooseStationId = true;
                startActivity(OrderDetailArrivalV2Activity.newIntent(getContext(), orderItemB));
                onBackPressed();
                return super.OnSucceed(result);
            }
        });
    }


    private void loop() {
        if (timer != null) return;
        //5秒后请求一次
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                RetrofitClient.getDefault()
                        .create(OrderDetailCase.class)
                        .getOrderList(orderItemB.getId() + "")
                        .compose(RxSchedulers.<NormalResponseB<OrderDetailB>>toMain())
                        .subscribe(new OnRequestObserver<OrderDetailB>() {

                            @Override
                            protected boolean OnSucceed(OrderDetailB result) {
                                if (result.getPayment_status().equals("approve")) {
                                    orderItemB.setPayment_status("approve");
                                    if (showPayCodeDialog != null) showPayCodeDialog.dismiss();
                                    unLoop();
                                }
                                return true;
                            }

                        });
            }
        }, 0, 3000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unLoop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void unLoop() {
        if (timer != null) timer.cancel();
    }

}
