package com.sjtubus.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.sjtubus.R;
import com.sjtubus.model.Station;
import com.sjtubus.utils.MyLocationListener;
import com.sjtubus.utils.MyMapStatusChangeListener;
import com.sjtubus.utils.MyMarkerClickListener;
import com.yinglan.scrolllayout.ScrollLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MapActivity extends BaseActivity {

    //百度地图相关
    private MapView mMapView;
    private MapStatusUpdate msu = null;
    private BaiduMap mBaiduMap;
    private Float zoomLevel = 16.0f;

    //定位相关
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener;
    private static final int BAIDU_READ_PHONE_STATE =100;

    //覆盖物相关
    private List<Marker> markers = new ArrayList<>();
    private List<Station> stations = new ArrayList<>();
    private Map<String,BitmapDescriptor> bitmaps = new ArrayMap<String, BitmapDescriptor>();
    private BaiduMap.OnMarkerClickListener mMarkClickListener = null;
    private MyMapStatusChangeListener mMapStatusChangeListener = null;

    //搜索相关
    RoutePlanSearch mSearch = null;
    RouteLine route = null;  //路线
    OverlayManager routeOverlay = null;  //该类提供一个能够显示和管理多个Overlay的基类
    boolean useDefaultIcon = true;
    DrivingRoutePlanOption routePlan = new DrivingRoutePlanOption();

    //站点详细信息相关
    private ScrollLayout mScrollLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPermission();
        initView();
        initLocation();
        initRoutePlan();
        addMarker();
    }

    public int getContentViewId(){
        return R.layout.activity_map;
    }

    private void getPermission(){
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
        msu = MapStatusUpdateFactory.zoomTo(zoomLevel);// 设置地图初始化缩放比例
        mBaiduMap.setMapStatus(msu);
        msu = MapStatusUpdateFactory.newLatLng(new LatLng(31.03201,121.443287));// 设置地图初始中心
        mBaiduMap.setMapStatus(msu);

        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);

        /**设置 setting*/
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (this.getResources().getDisplayMetrics().heightPixels * 0.3));
        mScrollLayout.setExitOffset(-100);
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToExit();
        mScrollLayout.getBackground().setAlpha(0);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mScrollLayout.scrollToExit();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                mScrollLayout.scrollToExit();
                return false;
            }
        });
        mMapStatusChangeListener = new MyMapStatusChangeListener(mScrollLayout);
        mBaiduMap.setOnMapStatusChangeListener(mMapStatusChangeListener);
    }

    private void initLocation(){
        myListener = new MyLocationListener(mBaiduMap);
        myListener.setLocation(false);//设置不以自己为中心
        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数

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
        stations = getData();

        PlanNode stNode = PlanNode.withLocation(new LatLng(stations.get(0).getLatitude(),stations.get(0).getLongitude()));//菁菁堂
        List<PlanNode> pbNode = new ArrayList<PlanNode>();
        for(int i = 1; i < stations.size(); i++){
            PlanNode node_temp  = PlanNode.withLocation(new LatLng(stations.get(i).getLatitude(),stations.get(i).getLongitude()));
            pbNode.add(node_temp);
        }
        //开始规划路线
        routePlan.from(stNode).passBy(pbNode).to(stNode);
        mSearch.drivingSearch(routePlan);

    }

    private void addMarker(){
        initBitmap();
        for(Station station : stations){
            BitmapDescriptor bd_temp = bitmaps.get(station.getName() + "_smallZoom");
            MarkerOptions marker_temp = new MarkerOptions()
                    .position(new LatLng(station.getLatitude(),station.getLongitude()))
                    .icon(bd_temp).anchor(0.5f, 0.5f).zIndex(7);
            //添加marker
            Marker marker = (Marker) mBaiduMap.addOverlay(marker_temp);
            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
            Bundle bundle = new Bundle();
            //info必须实现序列化接口
            bundle.putSerializable("info", station);
            marker.setExtraInfo(bundle);

            markers.add(marker);
        }
        mMarkClickListener = new MyMarkerClickListener(mScrollLayout);
        mBaiduMap.setOnMarkerClickListener(mMarkClickListener);
        mMapStatusChangeListener.setMarkers(markers);
        mMapStatusChangeListener.setBitmaps(bitmaps);
    }

    private void initBitmap(){
        BitmapDescriptor bd_temp ;
        View v_temp = LayoutInflater.from(getApplicationContext()).inflate(R.layout.map_marker, null);//加载自定义的布局
        ImageView img_temp = (ImageView) v_temp.findViewById(R.id.baidumap_custom_img);//获取自定义布局中的imageview
        img_temp.setImageResource(R.drawable.icon_gcoding);//设置marker的图标
        TextView tv_temp = (TextView) v_temp.findViewById(R.id.baidumap_custom_text);//获取自定义布局中的textview

        for(Station station : stations){
            tv_temp.setText(station.getName());//设置站点名
            bd_temp = BitmapDescriptorFactory.fromView(v_temp);
            bitmaps.put(station.getName() + "_bigZoom",bd_temp);

            tv_temp.setText("");//小缩略图时站点名不显示
            bd_temp = BitmapDescriptorFactory.fromView(v_temp);
            bitmaps.put(station.getName() + "_smallZoom",bd_temp);
        }
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
        //refreshMarker.onResume();
    }

    @Override
    public void onStart(){
        super.onStart();
        if (!mLocationClient.isStarted()){
            mLocationClient.start();//开启定位
        }
        //refreshMarker.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        //refreshMarker.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();//停止定位
        //refreshMarker.onStop();
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

            /*@Override
            public BitmapDescriptor getPassMarker() {
                if (useDefaultIcon) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
                }
                return null;
            }*/

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

            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                routeOverlay = overlay;
                //mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));  //设置路线数据
                overlay.addToMap(); //将所有overlay添加到地图中
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

    private class refreshThread extends Thread{
        private boolean isStop = false;
        @Override
        public void run() {
            super.run();
            while(!isStop){
                if(mBaiduMap.getMapStatus().zoom != zoomLevel){
                    mBaiduMap.clear();//清除所有overlay
                    routeOverlay.addToMap();//重新添加Route层
                    addMarker();//添加Marker层
                }
                SystemClock.sleep(1000L);//每隔一秒判断地图是否发生缩放
            }
            return;
        }

        public void onStop(){
            this.isStop = true;
        }
        public void onPause(){
            this.isStop = true;
        }
        public void onResume(){
            this.isStop = false;
            super.start();
        }
    }

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        //实现滑动时的背景变化
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
        }

        @Override
        public void onChildScroll(int top) {
        }
    };
    //数据应当从数据库读取
    private List<Station> getData(){
        List<Station> result = new ArrayList<Station>();
        Station temp = null;

        temp = new Station("菁菁堂",31.024769,121.436316);
        String[] AntiClockLoop = {
                "07:30",
                "07:45",
                "08:00",
                "08:15",
                "08:25",
                "08:40",
                "09:00",
                "09:20",
                "09:40",
                "10:00",
                "10:20",
                "10:40",
                "11:00",
                "11:20",
                "11:40",
                "12:00",
                "13:00",
                "13:20",
                "13:40",
                "14:00",
                "14:20",
                "14:40",
                "15:00",
                "15:20",
                "15:40",
                "16:00",
                "16:20",
                "16:30",
                "17:00"};
        temp.setAntiClockLoop(Arrays.asList(AntiClockLoop));
        String[] AntiClockNonLoop = {
                "17:15",
                "17:30",
                "17:50",
                "18:00",
                "19:00",
                "20:10"
        };
        temp.setAntiClockNonLoop(Arrays.asList(AntiClockNonLoop));
        String[] ClockLoop = {
                "08:30",
                "08:50",
                "09:10",
                "09:30",
                "10:00",
                "10:30",
                "11:00",
                "11:30",
                "12:30",
                "13:30",
                "14:00",
                "14:30",
                "15:00",
                "15:30",
                "16:00"
        };
        temp.setClockLoop(Arrays.asList(ClockLoop));
        String[] ClockNonLoop = {
                "16:30"
        };
        temp.setClockNonLoop(Arrays.asList(ClockNonLoop));
        //setVacAntiClockLoop();
        //setVacAntiClockNonLoop();
        //setVacClockLoop();
        //setVacClockNonLoop();
        result.add(temp);
        temp = new Station("校医院",31.025864,121.439918);
        result.add(temp);
        temp = new Station("东上院",31.027945,121.445348);
        result.add(temp);
        temp = new Station("东中院",31.030099,121.444427);
        result.add(temp);
        temp = new Station("新图书馆",31.031666,121.44383);
        result.add(temp);
        temp = new Station("行政B楼",31.032865,121.447585);
        result.add(temp);
        temp = new Station("电信学院",31.031593,121.448681);
        result.add(temp);
        temp = new Station("凯旋门",31.029484,121.452059);
        result.add(temp);
        temp = new Station("机动学院",31.032525,121.454574);
        result.add(temp);
        temp = new Station("庙门",31.035039,121.453428);
        result.add(temp);
        temp = new Station("船建学院",31.036837,121.451376);
        result.add(temp);
        temp = new Station("文选医学楼",31.037251,121.448506);
        result.add(temp);
        temp = new Station("学生服务中心",31.034389,121.439514);
        result.add(temp);
        temp = new Station("西区学生公寓",31.03319, 121.435849);
        result.add(temp);
        temp = new Station("第四餐饮大楼",31.031604,121.433221);
        result.add(temp);
        temp = new Station("华联生活中心",31.031128,121.436792);
        result.add(temp);
        temp = new Station("包玉刚图书馆",31.029047,121.437102);
        result.add(temp);
        temp = new Station("材料学院",31.028018,121.43456);
        result.add(temp);

        return result;
    }
}