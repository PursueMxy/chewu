package com.weimu.chewu.module.navi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.orhanobut.logger.Logger;
import com.weimu.chewu.AppData;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.PathPlanItemB;
import com.weimu.chewu.backend.bean.PositionB;
import com.weimu.chewu.backend.bean.TabEntity;
import com.weimu.chewu.module.navi.adapter.BusPathAdapter;
import com.weimu.chewu.module.navi.adapter.PathPlanListAdapter;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerMVPAdapter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.type.MyNaviType;
import com.weimu.chewu.widget.WMToast;
import com.weimu.gmap.core.map.overlay.BusRouteOverlay;
import com.weimu.gmap.core.map.overlay.DrivingRouteOverlay;
import com.weimu.gmap.core.map.overlay.RideRouteOverlay;
import com.weimu.gmap.core.map.overlay.WalkRouteOverlay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//路线规划
public class PathPlanActivity extends BaseActivity {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_path_plan;
    }

    //地图
    @BindView(R.id.map)
    MapView mMapView;
    //底部导航操作
    @BindView(R.id.cl_navi)
    ConstraintLayout clNavi;
    //公交列表
    @BindView(R.id.recyclerview_bus)
    RecyclerView recyclerView_bus;

    @BindView(R.id.tv_from)
    TextView tvFrom;
    @BindView(R.id.tv_to)
    TextView tvTo;

    @BindView(R.id.tab_navi_type)
    CommonTabLayout tabLayout;

    @BindView(R.id.recyclerview_path_plan)
    RecyclerView recyclerView;

    @BindView(R.id.btn_navi)
    TextView btnNavi;//导航按钮


    private MyNaviType naviType = MyNaviType.DRIVE;


    private PathPlanListAdapter mAdapter;
    private List<PathPlanItemB> pathPlanList = new ArrayList<>();


    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();


    private AMap aMap;
    private RouteSearch routeSearch;

    private PositionB from;
    private PositionB to;

    private LatLonPoint startPoint;
    private LatLonPoint endPoint;

    private PathPlanItemB currentPath;

    private BusPathAdapter busPathAdapter;

    private int orderId;//订单ID

    public static Intent newIntent(Context context, int orderId, PositionB to) {
        Intent intent = new Intent(context, PathPlanActivity.class);
        intent.putExtra("to", to);
        intent.putExtra("orderId", orderId);
        return intent;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        Intent intent = getIntent();

        from = AppData.getCurrentPosition();
        orderId = getIntent().getIntExtra("orderId", -1);
        to = (PositionB) intent.getSerializableExtra("to");


        startPoint = new LatLonPoint(from.getLatitude(), from.getLongitude());
        endPoint = new LatLonPoint(to.getLatitude(), to.getLongitude());

    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        //initToolBar();
        initTopToolBar();
        initPathRecyclerView();
        initMap();
        initPath();
        initBusList();
    }

    private void initBusList() {
        busPathAdapter = new BusPathAdapter(getContext());
        busPathAdapter.setOnItemClick(new BaseRecyclerMVPAdapter.OnItemClick<PathPlanItemB>() {
            @Override
            public void onClick(PathPlanItemB item, int position) {
                showNormalOperation();
                calculateBusPath(item.getBusPath());
            }
        });
        recyclerView_bus.setItemAnimator(new DefaultItemAnimator());
        recyclerView_bus.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView_bus.setAdapter(busPathAdapter);
    }

    //显示正常操作
    private void showNormalOperation() {
        mMapView.setVisibility(View.VISIBLE);
        clNavi.setVisibility(View.VISIBLE);
        recyclerView_bus.setVisibility(View.GONE);
    }

    //显示公交操作
    private void showBustOperation() {
        mMapView.setVisibility(View.GONE);
        clNavi.setVisibility(View.GONE);
        recyclerView_bus.setVisibility(View.VISIBLE);
    }


    private void initPathRecyclerView() {
        mAdapter = new PathPlanListAdapter(getContext());
        mAdapter.setOnItemClick(new BaseRecyclerMVPAdapter.OnItemClick<PathPlanItemB>() {
            @Override
            public void onClick(PathPlanItemB item, int position) {
                currentPath = item;
                mAdapter.setCheckItemPosition(position);

                switch (naviType) {
                    case DRIVE:
                        calculateDrivePath(item.getDrivePath());
                        break;
                    case BUS:
                        calculateBusPath(item.getBusPath());
                        break;
                    case RIDE:
                        calculateRidePath(item.getRidePath());
                        break;
                    case WALK:
                        calculateWalkPath(item.getWalkPath());
                        break;
                }
            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mAdapter);
    }

    private void initTopToolBar() {
        tvFrom.setText(from.getAddress());
        tvTo.setText(to.getAddress());

        for (MyNaviType item : MyNaviType.values()) {
            mTabEntities.add(new TabEntity(item.name));
        }
        tabLayout.setTabData(mTabEntities);

        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0:
                        naviType = MyNaviType.DRIVE;
                        mAdapter.setNaviType(naviType);
                        btnNavi.setVisibility(View.VISIBLE);

                        startDrive();

                        break;
                    case 1:
                        naviType = MyNaviType.BUS;
                        mAdapter.setNaviType(naviType);
                        btnNavi.setVisibility(View.GONE);
                        startBus();
                        break;
                    case 2:
                        naviType = MyNaviType.RIDE;
                        mAdapter.setNaviType(naviType);
                        btnNavi.setVisibility(View.VISIBLE);
                        startRide();
                        break;
                    case 3:
                        naviType = MyNaviType.WALK;
                        mAdapter.setNaviType(naviType);
                        btnNavi.setVisibility(View.VISIBLE);
                        startWalk();
                        break;
                }


            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    //返回
    @OnClick(R.id.iv_arrow_back)
    public void back() {
        onBackPressed();
    }

    //开始导航
    @OnClick(R.id.btn_navi)
    public void clickToNavi() {
        Poi start = new Poi(from.getAddress(), new LatLng(from.getLatitude(), from.getLongitude()), "");
        /**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
        Poi end = new Poi(to.getAddress(), new LatLng(to.getLatitude(), from.getLongitude()), "");

//        AmapNaviParams amapNaviParams = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER);
//        amapNaviParams.setUseInnerVoice(true);
//        AmapNaviPage.getInstance().showRouteActivity(getContext(), amapNaviParams, null);
        WMToast.normal("导航开启中...");
        startActivity(NaviActivity.newIntent(getContext(), currentPath.getPosition(), naviType));
    }


    private void initMap() {
        aMap = mMapView.getMap();

        //开始定位
        MyLocationStyle myLocationStyle = new MyLocationStyle();

        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.main_location_me));
        //定位类型
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        //myLocationStyle.anchor(0.0f,1.0f);

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。

        aMap.setMyLocationEnabled(false);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。


        //UI设置
        UiSettings uiSettings = aMap.getUiSettings();
        //隐藏默认的缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        //显示默认的定位按钮
        uiSettings.setMyLocationButtonEnabled(false);
        //设置导航针
        uiSettings.setCompassEnabled(false);
        //关闭旋转手势
        uiSettings.setRotateGesturesEnabled(false);
        //关闭倾斜手势
        uiSettings.setTiltGesturesEnabled(false);
    }


    private void initPath() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
                //公交
                Logger.e("公交 ==================================");

                if (i == 1000) {
                    pathPlanList.clear();
                    List<BusPath> paths = busRouteResult.getPaths();
                    for (BusPath item : paths) {
                        float distance = item.getDistance();//距离
                        long duration = item.getDuration();//时间
                        Logger.e("距离=" + distance + " 时间=" + duration);


                        PathPlanItemB pathPlanItemB = new PathPlanItemB();
                        pathPlanItemB.setDistance((distance / 1000) + "");
                        pathPlanItemB.setTime(duration);
                        pathPlanItemB.setBusPath(item);
                        pathPlanItemB.setPosition(to);
                        pathPlanList.add(pathPlanItemB);
                    }
                    mAdapter.setDataToAdapter(pathPlanList);
                    currentPath = mAdapter.getItem(0);

                    //特殊给予巴士列表
                    recyclerView_bus.smoothScrollToPosition(0);
                    busPathAdapter.setDataToAdapter(pathPlanList);

                    BusPath busPath = busRouteResult.getPaths().get(0);
                    calculateBusPath(busPath);

                } else {
                    Logger.e("计算驾驶路径失败");
                }
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                //汽车
                Logger.e("驾驶 ==================================");

                if (i == 1000) {
                    pathPlanList.clear();
                    List<DrivePath> paths = driveRouteResult.getPaths();
                    for (DrivePath item : paths) {
                        float distance = item.getDistance();//距离
                        long duration = item.getDuration();//时间
                        int totalTrafficlights = item.getTotalTrafficlights();//红绿灯
                        Logger.e("距离=" + distance + " 时间=" + duration + " 红绿灯=" + totalTrafficlights);


                        PathPlanItemB pathPlanItemB = new PathPlanItemB();
                        pathPlanItemB.setDistance((distance / 1000) + "");
                        pathPlanItemB.setTime(duration);
                        pathPlanItemB.setTrafficeLight(totalTrafficlights);
                        pathPlanItemB.setDrivePath(item);
                        pathPlanItemB.setPosition(to);
                        pathPlanList.add(pathPlanItemB);
                    }
                    mAdapter.setDataToAdapter(pathPlanList);
                    currentPath = mAdapter.getItem(0);

                    DrivePath drivePath = driveRouteResult.getPaths().get(0);
                    calculateDrivePath(drivePath);

                } else {
                    Logger.e("计算驾驶路径失败");
                }


            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                //步行
                Logger.e("步行 ==================================");
                if (i == 1000) {
                    pathPlanList.clear();
                    List<WalkPath> paths = walkRouteResult.getPaths();
                    for (WalkPath item : paths) {
                        float distance = item.getDistance();//距离
                        long duration = item.getDuration();//时间
                        Logger.e("距离=" + distance + " 时间=" + duration);


                        PathPlanItemB pathPlanItemB = new PathPlanItemB();
                        pathPlanItemB.setDistance((distance / 1000) + "");
                        pathPlanItemB.setTime(duration);
                        pathPlanItemB.setWalkPath(item);
                        pathPlanItemB.setPosition(to);
                        pathPlanList.add(pathPlanItemB);
                    }
                    mAdapter.setDataToAdapter(pathPlanList);
                    currentPath = mAdapter.getItem(0);

                    WalkPath drivePath = walkRouteResult.getPaths().get(0);
                    calculateWalkPath(drivePath);
                } else {
                    Logger.e("计算步行路径失败");
                }
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
                //骑行
                Logger.e("骑行 ==================================");
                if (i == 1000) {
                    pathPlanList.clear();
                    List<RidePath> paths = rideRouteResult.getPaths();
                    for (RidePath item : paths) {
                        float distance = item.getDistance();//距离
                        long duration = item.getDuration();//时间
                        Logger.e("距离=" + distance + " 时间=" + duration);


                        PathPlanItemB pathPlanItemB = new PathPlanItemB();
                        pathPlanItemB.setDistance((distance / 1000) + "");
                        pathPlanItemB.setTime(duration);
                        pathPlanItemB.setRidePath(item);
                        pathPlanItemB.setPosition(to);
                        pathPlanList.add(pathPlanItemB);
                    }
                    mAdapter.setDataToAdapter(pathPlanList);
                    currentPath = mAdapter.getItem(0);

                    RidePath ridePath = rideRouteResult.getPaths().get(0);
                    calculateRidePath(ridePath);
                } else {
                    Logger.e("计算步行路径失败");
                }
            }
        });
        startDrive();
    }

    //开始驾驶
    private void startDrive() {
        showProgressBar();
        showNormalOperation();

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_MULTI_STRATEGY_FASTEST_SHORTEST, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    //开始公交
    private void startBus() {
        showProgressBar();
        showBustOperation();

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BUS_DEFAULT, AppData.getCurrentPosition().getCity(), 0);
        routeSearch.calculateBusRouteAsyn(query);
    }

    //骑行
    private void startRide() {
        showProgressBar();
        showNormalOperation();

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, RouteSearch.RidingDefault);
        routeSearch.calculateRideRouteAsyn(query);
    }

    //步行
    private void startWalk() {
        showProgressBar();
        showNormalOperation();

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WALK_DEFAULT);
        routeSearch.calculateWalkRouteAsyn(query);
    }


    //计算驾驶的 路径规划 并显示
    private void calculateDrivePath(DrivePath drivePath) {
        hideProgressBar();
        aMap.clear();// 清理地图上的所有覆盖物
        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(getContext(), aMap, drivePath, startPoint, endPoint, null);
        drivingRouteOverlay.removeFromMap();
        drivingRouteOverlay.addToMap();
        drivingRouteOverlay.zoomToSpan();
    }

    //步行 路径规划
    private void calculateWalkPath(WalkPath walkPath) {
        hideProgressBar();
        aMap.clear();// 清理地图上的所有覆盖物
        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(getContext(), aMap, walkPath, startPoint, endPoint);
        walkRouteOverlay.removeFromMap();
        walkRouteOverlay.addToMap();
        walkRouteOverlay.zoomToSpan();
    }


    //骑行 路径规划
    private void calculateRidePath(RidePath ridePath) {
        hideProgressBar();
        aMap.clear();// 清理地图上的所有覆盖物
        RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(getContext(), aMap, ridePath, startPoint, endPoint);
        rideRouteOverlay.removeFromMap();
        rideRouteOverlay.addToMap();
        rideRouteOverlay.zoomToSpan();
    }


    //公交 路径规划
    private void calculateBusPath(BusPath busPath) {
        hideProgressBar();
        aMap.clear();// 清理地图上的所有覆盖物
        BusRouteOverlay busRouteOverlay = new BusRouteOverlay(getContext(), aMap, busPath, startPoint, endPoint);
        busRouteOverlay.removeFromMap();
        busRouteOverlay.addToMap();
        busRouteOverlay.zoomToSpan();
    }

    //以下不用管
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
