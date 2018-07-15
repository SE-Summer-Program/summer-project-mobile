package com.sjtubus.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import com.sjtubus.R;
import com.sjtubus.model.Station;
import com.sjtubus.utils.MyLocationListener;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity {

    //百度地图相关
    private MapView mMapView;
    private MapStatusUpdate msu = null;
    private BaiduMap mBaiduMap;

    //定位相关
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener;
    private static final int BAIDU_READ_PHONE_STATE =100;

    //Marker相关
    private List<LatLng> latLng = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();
    private List<OverlayOptions> overlayOptions = new ArrayList<>();
    private List<Station> stations = new ArrayList<>();

    //搜索相关
    RoutePlanSearch mSearch = null;
    RouteLine route = null;  //路线
    OverlayManager routeOverlay = null;  //该类提供一个能够显示和管理多个Overlay的基类
    boolean useDefaultIcon = true;
    DrivingRoutePlanOption routePlan = new DrivingRoutePlanOption();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        initPermission();
        initView();
        initLocation();
        initRoutePlan();
    }

    private void initPermission(){
        //h获得定位权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MapActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_READ_PHONE_STATE);
        }
    }

    private void initView(){
        //获取地图控件引用
        mMapView = findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setCompassEnable(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置为卫星显示
        //msu = MapStatusUpdateFactory.zoomTo(15.0f);// 设置地图初始化缩放比例
        //mBaiduMap.setMapStatus(msu);
        //mBaiduMap.setCompassIcon(BitmapFactory.
        //        decodeResource(getResources(),R.mipmap.compass));

    }

    private void initLocation(){
        myListener = new MyLocationListener(mBaiduMap);
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5*60*1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedLocationDescribe(true);
        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的位置信息，此处必须为true
        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;//定位跟随态
        //mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;   //默认为 LocationMode.NORMAL 普通态
        //mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;  //定位罗盘态

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        BitmapDescriptorFactory mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);
//        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
//        mBaiduMap.setMyLocationConfiguration(config);
    }

    private void initRoutePlan(){
        //初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        MyGetRoutePlanResultListener routeListener = new MyGetRoutePlanResultListener();
        mSearch.setOnGetRoutePlanResultListener(routeListener);
        //此处暂时为硬编码，应导入数据库数据
        //拾取坐标系统给的经纬度是反的！！！切记！！！
        PlanNode stNode = PlanNode.withLocation(new LatLng(31.024769,121.436316));//菁菁堂
        PlanNode enNode = PlanNode.withLocation(new LatLng(31.024769,121.436316));//菁菁堂
        List<PlanNode> paNode = new ArrayList<PlanNode>();
        PlanNode p1  = PlanNode.withLocation(new LatLng(31.025864,121.439918));//校医院
        PlanNode p2  = PlanNode.withLocation(new LatLng(31.027945,121.445348));//东上院
        PlanNode p3  = PlanNode.withLocation(new LatLng(31.030099,121.444427));//东中院
        PlanNode p4  = PlanNode.withLocation(new LatLng(31.031666,121.44383)); //新图书馆
        PlanNode p5  = PlanNode.withLocation(new LatLng(31.032865,121.447585));//行政B楼
        PlanNode p6  = PlanNode.withLocation(new LatLng(31.031593,121.448681));//电信学院
        PlanNode p7  = PlanNode.withLocation(new LatLng(31.029484,121.452059));//凯旋门
        PlanNode p8  = PlanNode.withLocation(new LatLng(31.032525,121.454574));//机动学院
        PlanNode p9  = PlanNode.withLocation(new LatLng(31.035039,121.453428));//庙门
        PlanNode p10 = PlanNode.withLocation(new LatLng(31.036837,121.451376));//船建学院
        PlanNode p11 = PlanNode.withLocation(new LatLng(31.037251,121.448506));//文选医学楼
        PlanNode p12 = PlanNode.withLocation(new LatLng(31.034389,121.439514));//学生服务中心
        PlanNode p13 = PlanNode.withLocation(new LatLng(31.03319, 121.435849));//西区学生公寓
        PlanNode p14 = PlanNode.withLocation(new LatLng(31.031604,121.433221));//第四餐饮大楼
        PlanNode p15 = PlanNode.withLocation(new LatLng(31.031128,121.436792));//华联生活中心
        PlanNode p16 = PlanNode.withLocation(new LatLng(31.029047,121.437102));//包玉刚图书馆
        PlanNode p17 = PlanNode.withLocation(new LatLng(31.028018,121.43456)); //材料学院
        paNode.add(p1);
        paNode.add(p2);
        paNode.add(p3);
        paNode.add(p4);
        paNode.add(p5);
        paNode.add(p6);
        paNode.add(p7);
        paNode.add(p8);
        paNode.add(p9);
        paNode.add(p10);
        paNode.add(p11);
        paNode.add(p12);
        paNode.add(p13);
        paNode.add(p14);
        paNode.add(p15);
        paNode.add(p16);
        paNode.add(p17);
        //开始规划路线
        routePlan.from(stNode).passBy(paNode).to(enNode);
        mSearch.drivingSearch(routePlan);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(myListener);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (!mLocationClient.isStarted())
        {
            mLocationClient.start();//开启定位
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();//停止定位
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "成功获取位置~", Toast.LENGTH_SHORT).show();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

            @Override
            public BitmapDescriptor getStartMarker() {
                if (useDefaultIcon) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
                }
                return null;
            }

            @Override
            public BitmapDescriptor getTerminalMarker() {
                if (useDefaultIcon) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
                }
                return null;
            }
    }

    private class MyGetRoutePlanResultListener implements
            OnGetRoutePlanResultListener {

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            if (result.error == SearchResult.ERRORNO.PERMISSION_UNFINISHED ||
                    result.error == SearchResult.ERRORNO.REQUEST_ERROR) {
                mSearch.drivingSearch(routePlan);
            }
            else if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR ) {
                Toast.makeText(MapActivity.this, result.error.toString(), Toast.LENGTH_SHORT).show();
            }

            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                //nodeIndex = -1;
                //mBtnPre.setVisibility(View.VISIBLE);
                //mBtnNext.setVisibility(View.VISIBLE);
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                routeOverlay = overlay;
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));  //设置路线数据
                overlay.addToMap(); //将所有overlay添加到地图中
                overlay.zoomToSpan();//缩放地图
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult arg0) {
        }

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult result) {
        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult result) {
        }
    }
}