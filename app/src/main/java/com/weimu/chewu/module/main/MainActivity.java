package com.weimu.chewu.module.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.weimu.chewu.AppData;
import com.weimu.chewu.BuildConfig;
import com.weimu.chewu.Const;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.AppSharePreB;
import com.weimu.chewu.backend.bean.OrderInMapB;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.dialog.ContactClientDialog;
import com.weimu.chewu.module.city.CityListActivity;
import com.weimu.chewu.module.contract_service.ContractServiceActivity;
import com.weimu.chewu.module.loginx.LoginActivity;
import com.weimu.chewu.module.message.MessageCenterActivity;
import com.weimu.chewu.module.order.OrderPageActivity;
import com.weimu.chewu.module.user.UserInfoActivity;
import com.weimu.chewu.module.wallet.MyWalletActivity;
import com.weimu.chewu.origin.center.SharePreferenceCenter;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.view.BaseEventActivity;
import com.weimu.chewu.services.UpLoadLocationService;
import com.weimu.chewu.utils.SharedDataTool;
import com.weimu.chewu.utils.SystemUtils;
import com.weimu.chewu.utils.yuyin.OfflineResource;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;
import com.weimu.gmap.core.location.LocationCenter;
import com.weimu.gmap.core.map.GmapDelegator;
import com.weimu.universalib.interfaces.MyDrawerListener;
import com.weimu.universalib.origin.view.diaog.BaseDialog;
import com.weimu.universalib.utils.IntentUtils;
import com.weimu.universalib.utils.TextViewUtils;
import com.weimu.universalib.utils.WindowsUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

import static com.weimu.chewu.utils.yuyin.MainHandlerConstant.PRINT;
import static com.weimu.chewu.utils.yuyin.MainHandlerConstant.UI_CHANGE_INPUT_TEXT_SELECTION;
import static com.weimu.chewu.utils.yuyin.MainHandlerConstant.UI_CHANGE_SYNTHES_TEXT_SELECTION;

public class MainActivity extends BaseEventActivity implements MainContract.MainView {
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.map)
    MapView mMapView;

    @BindView(R.id.main_civ_head)
    CircleImageView civ_head;
    @BindView(R.id.main_tv_name)
    TextView tv_name;
    @BindView(R.id.view_unread)
    View view_unread;

    @BindView(R.id.tv_log)
    TextView tvLog;

    @BindView(R.id.tv_version)
    TextView tvVersion;

    private MainContract.MainPresenter mPresenter;
    private ToolBarManager toolBar;

    private boolean isDrawerOpened = false;//侧滑栏是否打开
    private boolean isLocationFirst = false;//是否第一次定位
    private AMapLocation amapLocation;

    private GmapDelegator delegator;

    private Timer timer;//循环获取订单列表

    private long mExitTime;//退出时间


    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new MainPresenterImpl(this);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initDrawerLayout();
        initToolBar();
        checkPermission();
        setAlias();
        startService();
        showDialogs();
        inintVersion();
    }


    private void inintVersion() {
        //版本号
        if (BuildConfig.DEBUG) {
            tvVersion.setText("测试版本：V" + BuildConfig.VERSION_BUG);
        } else {
            tvVersion.setText("V" + BuildConfig.VERSION_NAME);
        }
    }

    private void showDialogs() {
        if (!SystemUtils.isNotificationEnabled(getBaseContext())) {
            showNotificationTipDialog();
        } else {
            showWhiteListSettingDialog();
        }

    }

    //显示 设置白名单 dialog
    private void showWhiteListSettingDialog() {
        final SharePreferenceCenter instance = SharePreferenceCenter.getInstance();
        if (instance.getAppShareP().isShowWhiteListSetting()) return;
        new MaterialDialog.Builder(this)
                .title("提示！")
                .content("请将App加入白名单，以保证行驶轨迹的正确收集")
                .positiveText("好的")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        SystemUtils.enterWhiteListSetting(getContext());
                        AppSharePreB appShareP = instance.getAppShareP();
                        appShareP.setShowWhiteListSetting(true);
                        instance.setAppShareP(appShareP);
                    }
                })
                .negativeText("稍后")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        AppSharePreB appShareP = instance.getAppShareP();
                        appShareP.setShowWhiteListSetting(true);
                        instance.setAppShareP(appShareP);
                    }
                })
                .show();

    }


    private void startService() {
        Intent intent = new Intent(this, UpLoadLocationService.class);
        startService(intent);
    }

    //显示 打开通知 dialog
    private void showNotificationTipDialog() {

        new MaterialDialog.Builder(this)
                .title("提示！")
                .content("请打开通知开关，以方便以后接收通知。")
                .positiveText("好的")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        IntentUtils.openAppInfo(getBaseContext());
                    }
                })
                .negativeText("稍后")
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        showWhiteListSettingDialog();
                    }
                })
                .show();
    }


    @Override
    public void showMessageRedDot(boolean isShow) {
        if (isShow) {
            view_unread.setVisibility(View.VISIBLE);
        } else {
            view_unread.setVisibility(View.GONE);
        }
    }


    private void loop() {
        //5秒后请求一次
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                clickToRefresh();
            }
        }, 0, 5000);
    }

    private void unLoop() {
        timer.cancel();
    }


    private void checkPermission() {
        new RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) initMap();
                    }
                });

    }


    private void initMap() {
        delegator = new GmapDelegator(getContext(), mMapView);
        delegator.setGmapListener(new GmapDelegator.GmapListener() {
            @Override
            public void onMarkerClickListener(Marker marker) {
                //点击获取订单
                OrderInMapB orderInMapB = (OrderInMapB) marker.getObject();
                mPresenter.orderReceiving(marker, orderInMapB);
                //返回true时，地图不会自动居中
            }
        });
    }


    private void fillUserData() {
        UserB userB = UserCenter.getInstance().getUserShareP();
        if (userB.getCustomer().getAvatar() != null) {
            Glide.with(this).load(userB.getCustomer().getAvatar()).into(civ_head);
        } else {
            Glide.with(this).load(R.drawable.head_default_64).into(civ_head);
        }
        tv_name.setText(userB.getCustomer().getName());
    }


    private void initDrawerLayout() {
        //drawerLaout
        mDrawerLayout.addDrawerListener(new MyDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpened = false;
            }
        });
    }


    // 打开抽屉
    public void toggleDrawerLayout() {
        if (isDrawerOpened) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            mDrawerLayout.openDrawer(Gravity.START);
        }

    }


    private Marker locationMarker;//定位的图层
    private LatLng locationData;//定位的坐标


    private void resolveLocation(AMapLocation amapLocation) {
        this.amapLocation = amapLocation;
        String province = amapLocation.getProvince();//省信息
        String city = amapLocation.getCity();//城市信息
        String country = amapLocation.getDistrict();//城区信息

//        WMToast.normal("当前定位="+currentPosition.toString());

        //todo 我的定位数据
        locationData = LocationCenter.amapLocation2LatLng(amapLocation);

        //添加Marker显示定位位置
        if (locationMarker == null) {
            //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
            locationMarker = delegator.addMarker(new MarkerOptions().position(locationData)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.main_location_me)));
        } else {
            //已经添加过了，修改位置即可
            MarkerOptions options = locationMarker.getOptions();
            options.position(locationData);
        }

        //初始化定位一次
        if (!isLocationFirst) {
            isLocationFirst = true;
            delegator.moveToLocation(locationData);
            //设置标题
            toolBar.setTitle(city);
            clickToRefresh();
        }
    }


    //todo 定位按钮
    @OnClick(R.id.iv_location)
    public void clickToLocation() {
        toolBar.setTitle(AppData.getCurrentPosition().getCity());
        delegator.smoothMoveToLocation(locationData);
    }


    //todo 刷新按钮
    @OnClick(R.id.iv_refresh)
    public void clickToRefresh() {
        if (!UserCenter.getInstance().isUserLogin()) return;
        if (amapLocation != null)
            mPresenter.getOrderInMap(
                    amapLocation.getProvince(),
                    amapLocation.getCity(),
                    amapLocation.getDistrict());
    }


    private void initToolBar() {
        toolBar = ToolBarManager.with(this, getContentView())
                .setTitle("")
                .setLeftMenuIconRes(R.drawable.main_me)
                .setLeftMenuIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleDrawerLayout();
                    }
                }).setRightMenuIconRes(R.drawable.main_notification)
                .setRightMenuIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.setAllRead();
                        startActivity(MessageCenterActivity.newIntent(getContext()));
                    }
                });

        TextView toolbarTitle = toolBar.getTitle();
        toolbarTitle.setCompoundDrawablePadding(WindowsUtils.dip2px(10));
        TextViewUtils.setLeftRightDrawable(getContext(), toolbarTitle, R.drawable.main_location, R.drawable.main_pull_down);

        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(amapLocation.getCity()))
                    startActivityForResult(CityListActivity.newIntent(getContext(), amapLocation.getCity()), Const.INTENT_TO_CHOOSE_CITY);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.INTENT_TO_CHOOSE_CITY && resultCode == RESULT_OK) {
            String city = data.getStringExtra("city");
            delegator.searchCity(city);
            toolBar.setTitle(city);
        }
    }

    @OnClick({R.id.main_rl_logout, R.id.main_rl_customer, R.id.main_rl_order, R.id.main_rl_wallet, R.id.main_cl_userinfo})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.main_rl_order://订单
                startActivity(OrderPageActivity.newIntent(getContext()));
                break;
            case R.id.main_rl_customer://客服
                startActivity(ContractServiceActivity.newIntent(getContext()));
                break;
            case R.id.main_rl_wallet://钱包
                intent = new Intent(MainActivity.this, MyWalletActivity.class);
                startActivity(intent);
                break;
            case R.id.main_rl_logout://退出
                logoutDialog();
                break;
            case R.id.main_cl_userinfo://个人中心
//                speak();
                intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
//                AppData.soundUtil.playRaw(MainActivity.this,R.raw.ddd);
                break;
        }
    }

    //以下不用管


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        delegator.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegator.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getCurrentOrderID();
        delegator.onResume();
        mPresenter.getMessageList();
        fillUserData();
        loop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        delegator.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unLoop();
    }

    @Override
    protected void onDestroy() {
        delegator.onDestroy();
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (isDrawerOpened) {
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            WMToast.normal("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
            return;
        } else {
            moveTaskToBack(false);
        }
        super.onBackPressed();
    }


    //加载订单列表
    @Override
    public void loadOrderList(List<OrderInMapB> result) {
        delegator.clearMap();

        delegator.addMarker(locationMarker.getOptions());
        //显示所有订单
        for (OrderInMapB item : result) {
            String[] locations = item.getLocation().split(",");

            //todo 订单列表
            LatLng latLng = new LatLng(Double.parseDouble(locations[1]), Double.parseDouble(locations[0]));
            MarkerOptions options = new MarkerOptions().position(latLng);
            //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.main_location_other));
            options.icon(BitmapDescriptorFactory.fromView(getMarkerOrderDefault(item)));
            Marker marker = delegator.addMarker(options);
            marker.setObject(item);
        }

    }

    //获取自定义MakerView
    public View getMarkerOrderDefault(OrderInMapB be) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.marker_orders, null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        //计算距离
        LatLng orderLatlng = be.getLatlng();
        float distance = AMapUtils.calculateLineDistance(orderLatlng, locationData);
        double kmDistance = new BigDecimal((distance / 1000)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tv_title.setText("距离" + kmDistance + "km");
        return view;
    }

    @Override
    public void showContractDialog(final Marker marker, final OrderInMapB orderInMapB) {
//        mPresenter.getOrderInMap(
//                amapLocation.getProvince(),
//                amapLocation.getCity(),
//                amapLocation.getDistrict());
        //设置此订单已经在借贷状态
//        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.main_location_accepting));


        //todo 显示倒计时
        ContactClientDialog dialog = (ContactClientDialog) new ContactClientDialog().show(getContext());
        dialog.setOnOrderConfirmListener(new ContactClientDialog.OnOrderConfirmListener() {
            @Override
            public void confirm() {
                //todo 进入拨打电话界面
                showToast("接单成功");
                IntentUtils.gotoDialog(getContext(), orderInMapB.getPhone());
                mPresenter.confirmOrder(orderInMapB.getId());
            }

            @Override
            public void cancel() {
                mPresenter.cancelOrder(orderInMapB.getId());
            }
        });
        dialog.setOnDialogListener(new BaseDialog.OnDialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onDismiss() {
                clickToRefresh();
            }
        });
    }

    //退出登录成功
    @Override
    public void logoutSuccess() {
        SharePreferenceCenter center = SharePreferenceCenter.getInstance();
        AppSharePreB appShareP = center.getAppShareP();
        appShareP.setOrderId(-1);
        appShareP.setUploadPosition(null);
        center.setAppShareP(appShareP);
        JPushInterface.deleteAlias(getApplicationContext(), 1);
        UserCenter.getInstance().cleanUserShareP();
        SharePreferenceCenter.getInstance().cleanAppShareP();
        AppData.closeAllActivity();
        SharedDataTool.setBoolean(getContext(), "isAlias", false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    //接单失败
    @Override
    public void receiveOrderFail() {
        clickToRefresh();
    }

    @Override
    public void setAllReadSuccess() {
        view_unread.setVisibility(View.GONE);
    }


    /**
     * 退出登录弹窗
     */
    private void logoutDialog() {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
//        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_modify_phone_notify, null);
//        //初始化控件
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
//        //设置Dialog从窗体从中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
//        dialog.setCancelable(true);
        dialog.show();//显示对话框

        TextView tv_concel = inflate.findViewById(R.id.dialog_tv_cancel);
        TextView tv_confirm = inflate.findViewById(R.id.dialog_tv_confirm);
        TextView tv_notify = inflate.findViewById(R.id.dialog_tv_notify);
        tv_notify.setText("确定要退出登录吗？");
        tv_concel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mPresenter.doLogout();
            }
        });
    }

    /**
     * 设置推送别名
     */
    private void setAlias() {
        if (!UserCenter.getInstance().isUserLogin()) return;
        String alias = UserCenter.getInstance().getUserShareP().getCustomer().getPhone();
        // 调用 JPush 接口来设置别名。
        JPushInterface.setAlias(getContext(), 1, alias);
    }


    //合成语音
    protected void handle(Message msg) {
        int what = msg.what;
        switch (what) {
            case PRINT:
//                print(msg);
                break;
            case UI_CHANGE_INPUT_TEXT_SELECTION:
//                if (msg.arg1 <= mInput.getText().length()) {
//                    mInput.setSelection(0, msg.arg1);
//                }
                break;
            case UI_CHANGE_SYNTHES_TEXT_SELECTION:
//                SpannableString colorfulText = new SpannableString(mInput.getText().toString());
//                if (msg.arg1 <= colorfulText.toString().length()) {
//                    colorfulText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, msg.arg1, Spannable
//                            .SPAN_EXCLUSIVE_EXCLUSIVE);
//                    mInput.setText(colorfulText);
//                }
                break;
            default:
                break;
        }
    }


    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            Log.e("ERROR", "【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    //不断获取定位位置
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationChanged(AMapLocation amapLocation) {
        resolveLocation(amapLocation);
    }


    //不断获取日志信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMsg(String msg) {
        tvLog.setText(msg);
    }


}
