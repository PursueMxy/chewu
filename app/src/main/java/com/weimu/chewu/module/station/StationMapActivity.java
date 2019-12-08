package com.weimu.chewu.module.station;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.PositionB;
import com.weimu.chewu.backend.bean.StationListResultB;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.StationCase;
import com.weimu.chewu.module.navi.NaviActivity;
import com.weimu.chewu.origin.view.BaseEventActivity;
import com.weimu.chewu.type.MyNaviType;
import com.weimu.chewu.widget.WMToast;
import com.weimu.gmap.core.location.LocationCenter;
import com.weimu.gmap.core.map.GmapDelegator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

//监测站地图
public class StationMapActivity extends BaseEventActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_station_map;
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, StationMapActivity.class);
        return intent;
    }

    @BindView(R.id.map)
    MapView mMapView;

    @BindView(R.id.tv_position_current)
    TextView tvPositionCurrent;

    @BindView(R.id.tv_position_station)
    TextView tvPositionStation;

    @BindView(R.id.btn_navi)
    TextView tvNavi;

    private StationCase mCase;


    private GmapDelegator delegator;

    private Marker locationMarker;//定位的图层
    private LatLng locationData;//定位的坐标

    private boolean isLocationFirst = false;//是否第一次定位

    private StationListResultB.StationB currentStation;//当前的监测站

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mCase = new StationCaseImpl();
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        checkPermission();
        checkBtnNavi();
        LocationCenter.getInstance().init(this);

    }


    private void checkPermission() {
        new RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            initMap();
                        }
                    }
                });

    }

    private void initMap() {
        delegator = new GmapDelegator(getContext(), mMapView);
        delegator.setGmapListener(new GmapDelegator.GmapListener() {
            @Override
            public void onMarkerClickListener(Marker marker) {
                //点击获取监测站
                currentStation = (StationListResultB.StationB) marker.getObject();
                tvPositionStation.setText(currentStation.getAddress());
                checkBtnNavi();
            }
        });
    }


    //不断获取定位位置
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationChanged(AMapLocation amapLocation) {
        resolveLocation(amapLocation);
    }


    private void resolveLocation(AMapLocation amapLocation) {
        //todo 我的定位数据
        locationData = LocationCenter.amapLocation2LatLng(amapLocation);
        //添加Marker显示定位位置
        if (locationMarker == null) {
            //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
            locationMarker = delegator.addMarker(new MarkerOptions().position(locationData)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.main_location_me)));
        } else {
            //已经添加过了，修改位置即可
            locationMarker.setPosition(locationData);
        }

        //初始化定位一次
        if (!isLocationFirst) {
            isLocationFirst = true;
            delegator.moveToLocation(locationData);
            getStations();
        }
        tvPositionCurrent.setText(amapLocation.getAddress());
    }


    private void initToolBar() {
//        ToolBarManager.with(this, getContentView())
//                .setTitle("选择监测站")
//                .setNavigationIcon(R.drawable.back);
    }


    //todo 定位按钮
    @OnClick(R.id.iv_location)
    public void clickToLocation() {
        delegator.smoothMoveToLocation(locationData);
    }

    //左上角 返回按钮
    @OnClick(R.id.iv_arrow_back)
    public void arrowBack() {
        if (currentStation != null) {
            Intent intent = new Intent();
            intent.putExtra("station", currentStation);
            setResult(RESULT_OK, intent);

        }
        onBackPressed();
    }

    //导航按钮
    @OnClick(R.id.btn_navi)
    public void navi() {
        WMToast.normal("导航开启中...");
        PositionB positionB = new PositionB(currentStation.getAddress());
        positionB.setLatitude(currentStation.getLatlng().latitude);
        positionB.setLongitude(currentStation.getLatlng().longitude);
        startActivity(NaviActivity.newIntent(getContext(), positionB, MyNaviType.DRIVE));
    }

    private void checkBtnNavi() {
        if (currentStation == null) {
            tvNavi.setVisibility(View.GONE);
        } else {
            tvNavi.setVisibility(View.VISIBLE);
        }
    }

    //获取订单
    public void getStations() {
        mCase.getStations().subscribe(new OnRequestObserver<StationListResultB>() {

            @Override
            protected boolean OnSucceed(StationListResultB result) {
                showStationsMaker(result.getData());
                return true;
            }
        });
    }

    //显示监测站
    private void showStationsMaker(List<StationListResultB.StationB> result) {
        delegator.clearMap();


        delegator.addMarker(locationMarker.getOptions());

        StationListResultB.StationB closeStation = null;

        for (StationListResultB.StationB item : result) {
            if (closeStation == null) closeStation = item;
            float closeDistance = AMapUtils.calculateLineDistance(closeStation.getLatlng(), locationData);
            float itemDistance = AMapUtils.calculateLineDistance(item.getLatlng(), locationData);
            if (itemDistance < closeDistance) {
                closeStation = item;
            }
        }
        for (StationListResultB.StationB item : result) {

            LatLng latLng = item.getLatlng();

            Marker marker = null;

            if (item == closeStation) {
                marker = delegator.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromView(getMarkerStationClose(item))));
            } else {
                marker = delegator.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromView(getMarkerStationDefault(item))));
            }

            marker.setObject(item);
        }
    }


    //获取自定义MakerView
    public View getMarkerStationDefault(StationListResultB.StationB be) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.marker_station_default, null);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tv_title = view.findViewById(R.id.tv_title);
        //计算距离
        LatLng stationsLatlng = be.getLatlng();
        float distance = AMapUtils.calculateLineDistance(stationsLatlng, locationData);
        double kmDistance = new BigDecimal((distance / 1000)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tvName.setText(be.getName());
        tv_title.setText("距离" + kmDistance + "km");
        return view;
    }


    //获取自定义MakerView
    public View getMarkerStationClose(StationListResultB.StationB be) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.marker_station_closely, null);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tv_title = view.findViewById(R.id.tv_title);
        //计算距离
        LatLng stationsLatlng = be.getLatlng();
        float distance = AMapUtils.calculateLineDistance(stationsLatlng, locationData);
        double kmDistance = new BigDecimal((distance / 1000)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tvName.setText(be.getName());
        tv_title.setText("距离" + kmDistance + "km");
        return view;
    }


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
        delegator.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        delegator.onPause();
    }

    @Override
    protected void onDestroy() {
        delegator.onDestroy();
        super.onDestroy();
    }

}
