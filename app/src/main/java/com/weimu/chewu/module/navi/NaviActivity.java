package com.weimu.chewu.module.navi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.orhanobut.logger.Logger;
import com.weimu.chewu.AppData;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.PositionB;
import com.weimu.chewu.module.navi.interfaces.MyAMapNaviListener;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.type.MyNaviType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 导航界面
 */
public class NaviActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_navi;
    }


    public static Intent newIntent(Context context, PositionB to, MyNaviType naviType) {
        Intent intent = new Intent(context, NaviActivity.class);
        intent.putExtra("to", to);
        intent.putExtra("naviType", naviType);
        return intent;
    }


    @BindView(R.id.navi_view)
    AMapNaviView mAMapNaviView;

    private AMapNavi mAMapNavi;

    private PositionB from;
    private PositionB to;

    private List<NaviLatLng> start = new ArrayList<>();
    private List<NaviLatLng> end = new ArrayList<>();

    private MyNaviType naviType;


    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);

        from = AppData.getCurrentPosition();
        to = (PositionB) getIntent().getSerializableExtra("to");
        naviType = (MyNaviType) getIntent().getSerializableExtra("naviType");

        start.add(new NaviLatLng(from.getLatitude(), from.getLongitude()));
        end.add(new NaviLatLng(to.getLatitude(), to.getLongitude()));

        Logger.e("from=" + from.toString() + "  to=" + to.toString());
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);


        //获取 AMapNaviView 实例
        mAMapNaviView = findViewById(R.id.navi_view);
        //mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNaviView.onCreate(savedInstanceState);

        //AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        //关闭自动绘制路线（如果你想自行绘制路线的话，必须关闭！！！）
        //options.setAutoDrawRoute(false);
        //mAMapNaviView.setViewOptions(options);


        //选择
        AMapNaviViewOptions viewOptions = mAMapNaviView.getViewOptions();

        mAMapNaviView.setViewOptions(viewOptions);

        mAMapNaviView.setAMapNaviViewListener(new AMapNaviViewListener() {
            @Override
            public void onNaviSetting() {

            }

            @Override
            public void onNaviCancel() {
                onBackPressed();
            }

            @Override
            public boolean onNaviBackClick() {
                //点击左下角关闭按钮
                return false;
            }

            @Override
            public void onNaviMapMode(int i) {

            }

            @Override
            public void onNaviTurnClick() {

            }

            @Override
            public void onNextRoadClick() {

            }

            @Override
            public void onScanViewButtonClick() {

            }

            @Override
            public void onLockMap(boolean b) {

            }

            @Override
            public void onNaviViewLoaded() {

            }
        });


        //获取AMapNavi实例
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.setUseInnerVoice(true);
        //添加监听回调，用于处理算路成功
        mAMapNavi.addAMapNaviListener(new MyAMapNaviListener() {
            @Override
            public void onInitNaviSuccess() {
                switch (naviType) {
                    case DRIVE:
                        startDrive();
                        break;
                    case RIDE:
                        startRide();
                        break;
                    case WALK:
                        startWalk();
                        break;

                }

            }

            @Override
            public void onCalculateRouteSuccess(int[] ints) {
                //显示路径或开启导航
                Logger.e("规划路线成功");
                //规划路线成功
                mAMapNavi.startNavi(NaviType.GPS);
//                if (BuildConfig.DEBUG)
//                    mAMapNavi.startNavi(NaviType.EMULATOR);
//                else
//                    mAMapNavi.startNavi(NaviType.GPS);
            }
        });
    }

    private void startDrive() {
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }


        mAMapNavi.calculateDriveRoute(start, end, null, strategy);
    }

    private void startRide() {
        mAMapNavi.calculateRideRoute(start.get(0), end.get(0));
    }

    private void startWalk() {
        mAMapNavi.calculateWalkRoute(start.get(0), end.get(0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        mAMapNaviView.onDestroy();
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        super.onDestroy();
    }


}
