package com.weimu.gmap.core.map;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:你需要一台永动机
 * Date:2018/5/9 16:43
 * Description:
 */
public class GmapDelegator implements AMap.OnMarkerClickListener {

    private Context context;//上下文
    private MapView mMapView;//地图控件
    private AMap aMap;//地图类


    public GmapDelegator(Context context, MapView mMapView) {
        this.context = context;
        this.mMapView = mMapView;

        initMap();
    }

    private void initMap() {
        aMap = mMapView.getMap();

        //地图自身存在的 定位蓝点  默认不用
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.main_location_me));
        //定位类型
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
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

        //marker点击回调
        aMap.setOnMarkerClickListener(this);
    }


    //Marker点击
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (gmapListener != null) gmapListener.onMarkerClickListener(marker);
        return true;
    }

    //search city
    public void searchCity(String keyWord) {
        DistrictSearch search = new DistrictSearch(context);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords(keyWord);//传入关键字
        query.setShowBoundary(true);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(new DistrictSearch.OnDistrictSearchListener() {
            @Override
            public void onDistrictSearched(final DistrictResult districtResult) {
                //在回调函数中解析districtResult获取行政区划信息
                //在districtResult.getAMapException().getErrorCode()=1000时调用districtResult.getDistrict()方法
                //获取查询行政区的结果，详细信息可以参考DistrictItem类。
                if (districtResult.getAMapException().getErrorCode() == 1000) {
                    ArrayList<DistrictItem> district = districtResult.getDistrict();
                    LatLonPoint center = district.get(0).getCenter();
                    moveToLocation(new LatLng(center.getLatitude(), center.getLongitude()), 11);
                }
            }
        });//绑定监听器
        search.searchDistrictAnsy();//开始搜索
    }

    //获取aMap类
    public AMap getAmap() {
        return aMap;
    }


    //清除地图上的图标
    public void clearMap() {
        if (aMap == null) throw new NullPointerException("aMap must no null");
        aMap.clear();
    }

    //增加Marker
    public Marker addMarker(MarkerOptions markerOptions) {
        if (aMap == null) throw new NullPointerException("aMap must no null");
        return aMap.addMarker(markerOptions);
    }

    //滚动到具体位置
    public void smoothMoveToLocation(LatLng latLng) {
        smoothMoveToLocation(latLng, 17);
    }

    //滚动到具体位置
    public void smoothMoveToLocation(LatLng latLng, float level) {
        if (aMap != null && latLng != null) {
            //然后可以移动到定位点,使用animateCamera就有动画效果
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, level));
        }
    }

    //直接显示具体位置
    public void moveToLocation(LatLng latLng) {
        moveToLocation(latLng, 17);
    }

    //直接显示具体位置
    public void moveToLocation(LatLng latLng, float level) {
        if (aMap != null && latLng != null) {
            //然后可以移动到定位点,使用animateCamera就有动画效果
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, level));
        }
    }


    public void onSaveInstanceState(Bundle outState) {
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    public void onCreate(@Nullable Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
    }


    public void onResume() {
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    public void onPause() {
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    public void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }


    private GmapListener gmapListener;

    public void setGmapListener(GmapListener gmapListener) {
        this.gmapListener = gmapListener;
    }

    public interface GmapListener {
        void onMarkerClickListener(Marker marker);
    }

    //画线
    public Polyline addPolyline(PolylineOptions polylineOptions) {
        return aMap.addPolyline(polylineOptions);

    }

    //缩放到制定位置
    public void zoomToSpan(LatLng startPoint, LatLng endPoint) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        b.include(startPoint);
        b.include(endPoint);
        LatLngBounds bounds = b.build();
        if (startPoint != null) {
            if (aMap == null) {
                return;
            }
            try {
                aMap.animateCamera(CameraUpdateFactory
                        .newLatLngBounds(bounds, 100));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
