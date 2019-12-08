package com.weimu.chewu.module.pathtrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.orhanobut.logger.Logger;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.Position4PathB;
import com.weimu.chewu.backend.bean.OrderDetailB;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.OrderDetailCase;
import com.weimu.chewu.module.order_detail_ing.OrderDetailCaseImpl;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.gmap.core.map.GmapDelegator;
import com.weimu.gmap.core.map.overlay.DrivingPathTrackOverlay;
import com.weimu.gmap.core.map.overlay.TraceOverlay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 查看路径
 */
public class PathTrackActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_path_track;
    }

    public static Intent newIntent(Context context, OrderDetailB orderDetail) {
        Intent intent = new Intent(context, PathTrackActivity.class);
        intent.putExtra("orderDetail", orderDetail);
        return intent;
    }

    @BindView(R.id.map)
    MapView mMapView;

    private OrderDetailB orderDetail;
    private OrderDetailCase mCase;

    private GmapDelegator delegator;

    private RouteSearch routeSearch;

    private LatLonPoint startPoint;
    private LatLonPoint stationsPoint;
    private LatLonPoint endPoint;

    private LBSTraceClient mTraceClient;//道路纠偏


    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        orderDetail = (OrderDetailB) getIntent().getSerializableExtra("orderDetail");
        mCase = new OrderDetailCaseImpl();


        stationsPoint = new LatLonPoint(orderDetail.getStation().getLatlng().latitude, orderDetail.getStation().getLatlng().longitude);


        mTraceClient = new LBSTraceClient(this.getApplicationContext());
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        initMap();
        initPath();

        getLocations();
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setTitle("查看行驶路径")
                .setNavigationIcon(R.drawable.back);
    }

    private void getLocations() {
        mCase.getLocation(orderDetail.getId()).subscribe(new OnRequestObserver<List<Position4PathB>>() {
            @Override
            protected boolean OnSucceed(final List<Position4PathB> result) {
                if (result == null || result.size() <= 0) {
                    showToast("无有效的行驶路径");
                    onBackPressed();
                    return super.OnSucceed(result);
                }
                List<TraceLocation> traceLocation = new ArrayList<>();
                for (Position4PathB item : result) {
                    try {
                        double longitude = Double.parseDouble(item.getLng());
                        double latitude = Double.parseDouble(item.getLat());
                        float speed = Float.parseFloat(item.getSpeed()) * 3.6f;
                        float bearing = Float.parseFloat(item.getBearing());
                        Long time = Long.parseLong(item.getTime());
                        traceLocation.add(new TraceLocation(latitude, longitude, speed, bearing, time));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                mTraceClient.queryProcessedTrace(1, traceLocation, LBSTraceClient.TYPE_AMAP, new TraceListener() {
                    @Override
                    public void onRequestFailed(int lineID, String errorInfo) {
                        Logger.e("onRequestFailed info=" + errorInfo);

                        //纠偏前的线
                        List<LatLng> latLngs = new ArrayList<>();
                        for (Position4PathB item : result) {
                            latLngs.add(new LatLng(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLng())));
                        }
                        TraceOverlay overlay2 = new TraceOverlay(delegator.getAmap(), latLngs);

                        //起始点
                        LatLng startLoc = result.get(0).toLatLng();


                        startPoint = new LatLonPoint(startLoc.latitude, startLoc.longitude);
                        //结束点
                        LatLng endLoc = result.get(result.size() - 1).toLatLng();


                        endPoint = new LatLonPoint(endLoc.latitude, endLoc.longitude);
                        //画线
                        Logger.e("开始点=" + startPoint.toString() + " 结束点=" + endPoint.toString());
//                        delegator.addMarker(new MarkerOptions().position(new LatLng(startPoint.getLatitude(), startPoint.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_start)));
                        delegator.addMarker(new MarkerOptions().position(orderDetail.getLatlng()).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_pick_up)));
                        delegator.addMarker(new MarkerOptions().position(new LatLng(stationsPoint.getLatitude(), stationsPoint.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_station)));
                        delegator.addMarker(new MarkerOptions().position(new LatLng(endPoint.getLatitude(), endPoint.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_end)));
                        delegator.zoomToSpan(startLoc, endLoc);
                    }

                    @Override
                    public void onTraceProcessing(int i, int i1, List<LatLng> list) {
                        Logger.e("onTraceProcessing");

                    }

                    @Override
                    public void onFinished(int i, List<LatLng> list, int i1, int i2) {
                        Logger.e("onFinished");
                        //纠偏后的线
//                        TraceOverlay overlay1 = new TraceOverlay(delegator.getAmap(),list);
//                        overlay1.zoopToSpan();
                        //纠偏前的线
                        List<LatLng> latLngs = new ArrayList<>();
                        for (Position4PathB item : result) {
                            latLngs.add(item.toLatLng());
                        }
                        TraceOverlay overlay2 = new TraceOverlay(delegator.getAmap(), latLngs);
                        overlay2.zoopToSpan();

                        //起始点
                        LatLng startLoc = list.get(0);
                        startLoc = result.get(0).toLatLng();
                        startPoint = new LatLonPoint(startLoc.latitude, startLoc.longitude);

                        //结束点
                        LatLng endLoc = list.get(list.size() - 1);
                        endLoc = result.get(result.size() - 1).toLatLng();
                        endPoint = new LatLonPoint(endLoc.latitude, endLoc.longitude);

                        //画线
                        Logger.e("开始点=" + startPoint.toString() + " 结束点=" + endPoint.toString());
                        delegator.addMarker(new MarkerOptions().position(startLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_start)));
                        delegator.addMarker(new MarkerOptions().position(orderDetail.getLatlng()).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_pick_up)));
                        delegator.addMarker(new MarkerOptions().position(new LatLng(stationsPoint.getLatitude(), stationsPoint.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_station)));
                        delegator.addMarker(new MarkerOptions().position(endLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_end)));
                    }
                });


                return true;
            }
        });
    }

    //画线
    private void drawLine(List<LatLng> latLngs) {
        BitmapDescriptor mBlueTexture = BitmapDescriptorFactory
                .fromAsset("ic_gmap_line_blue.png");
        delegator.addPolyline(new PolylineOptions().
                addAll(latLngs)
                .setCustomTexture(mBlueTexture)
                .width(10f)
//                .color(Color.argb(255, 38, 56, 238))
                .useGradient(true));
    }


    //获取坐标
    public LatLng getLatlng(String location) {
        if (TextUtils.isEmpty(location)) {
            return null;
        }
        String[] split = location.split(",");
        double latitude = Double.parseDouble(split[1]);
        double longitude = Double.parseDouble(split[0]);
        return new LatLng(latitude, longitude);
    }

    private void initMap() {
        delegator = new GmapDelegator(getContext(), mMapView);
        delegator.setGmapListener(new GmapDelegator.GmapListener() {
            @Override
            public void onMarkerClickListener(Marker marker) {

            }
        });
    }


    private void initPath() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                //汽车
                Logger.e("驾驶 ==================================");

                if (i == 1000) {
                    DrivePath drivePath = driveRouteResult.getPaths().get(0);
                    calculateDrivePath(drivePath);

                } else {
                    Logger.e("计算驾驶路径失败");
                }


            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
            }
        });

    }

    //计算驾驶的 路径规划 并显示
    private void calculateDrivePath(DrivePath drivePath) {
        delegator.clearMap();// 清理地图上的所有覆盖物

        List<LatLonPoint> throughPointList = new ArrayList<>();
        throughPointList.add(new LatLonPoint(stationsPoint.getLatitude(), stationsPoint.getLongitude()));
        DrivingPathTrackOverlay drivingRouteOverlay = new DrivingPathTrackOverlay(getContext(), delegator.getAmap(), drivePath, startPoint, endPoint, throughPointList);
        drivingRouteOverlay.removeFromMap();
        drivingRouteOverlay.addToMap();
        drivingRouteOverlay.zoomToSpan();


        delegator.addMarker(new MarkerOptions().position(new LatLng(startPoint.getLatitude(), startPoint.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_pick_up)));
        delegator.addMarker(new MarkerOptions().position(new LatLng(stationsPoint.getLatitude(), stationsPoint.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_station)));
        delegator.addMarker(new MarkerOptions().position(new LatLng(endPoint.getLatitude(), endPoint.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track_end)));
    }


    //开始驾驶
    private void startDrive() {
        List<LatLonPoint> throughPointList = new ArrayList<>();
        throughPointList.add(new LatLonPoint(stationsPoint.getLatitude(), stationsPoint.getLongitude()));

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_MULTI_STRATEGY_FASTEST_SHORTEST, throughPointList, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
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
