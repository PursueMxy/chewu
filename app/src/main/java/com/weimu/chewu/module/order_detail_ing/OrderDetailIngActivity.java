package com.weimu.chewu.module.order_detail_ing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.weimu.chewu.Const;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.AppSharePreB;
import com.weimu.chewu.backend.bean.OrderDetailB;
import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.bean.PositionB;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.OrderDetailCase;
import com.weimu.chewu.module.navi.PathPlanActivity;
import com.weimu.chewu.module.order_detail_arrival.AddArrivalPictureActivity;
import com.weimu.chewu.module.order_detail_ing.adapter.MessageAdapter;
import com.weimu.chewu.origin.center.SharePreferenceCenter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;
import com.weimu.universalib.utils.IntentUtils;
import com.weimu.universalib.utils.SpannableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单详情  进行中
 */
public class OrderDetailIngActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_order_detail_ing;
    }


    public static Intent newIntent(Context context, OrderItemB orderItemB) {
        Intent intent = new Intent(context, OrderDetailIngActivity.class);
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

    private OrderItemB orderItemB;
    private OrderDetailCase mCase;
    private MessageAdapter mAdapter;

    private OrderDetailB result;


    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        orderItemB = (OrderItemB) getIntent().getSerializableExtra("orderItem");

        mCase = new OrderDetailCaseImpl();
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        initToolBar();
        initMessageList();
        //获取订单详情
        getOrderDetail();
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

    //到达接车地点
    @OnClick(R.id.tv_confirm)
    public void clickToConfirm() {
        int localOrderId;
        SharePreferenceCenter center = SharePreferenceCenter.getInstance();
        AppSharePreB appShareP = center.getAppShareP();
        localOrderId = appShareP.getOrderId();
        //如果本地没有orderid的话，那么才能够进行下一步，如果本地有orderid的话，则有定位未完成
        if (localOrderId == -1) {
            startActivityForResult(AddArrivalPictureActivity.newIntent(getContext(), orderItemB), Const.INTENT_TO_ADD_ARRIVAL_PICTURE);
        } else if (localOrderId==orderItemB.getId()){
            startActivityForResult(AddArrivalPictureActivity.newIntent(getContext(), orderItemB), Const.INTENT_TO_ADD_ARRIVAL_PICTURE);
        }else {
            WMToast.normal("有未完成订单！");
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
        if (requestCode == Const.INTENT_TO_ADD_ARRIVAL_PICTURE && resultCode == RESULT_OK) {
            onBackPressed();
        }
    }
}
