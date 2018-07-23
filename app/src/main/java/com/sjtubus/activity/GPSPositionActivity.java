//package com.sjtubus.activity;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.location.LocationProvider;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.baidu.mapapi.model.inner.GeoPoint;
//import com.sjtubus.R;
//import com.sjtubus.utils.ToastUtils;
//
//import java.util.List;
//
//public class GPSPositionActivity extends BaseActivity implements View.OnClickListener {
//
//    private EditText editText;
//    private Button startbtn;
//    private LocationManager locationManager;
//    private String locationProvider;
//    private static final String TAG = "GPSPositionActivity";
//
//    /*
//     * 生命周期管理 oncreate - onstart - onresume - onpause - onstop - ondestroy
//     */
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        locationManager.removeUpdates(locationListener);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        locationManager.removeUpdates(locationListener);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initViews();
//        startGPSservice();
//    }
//
//    @Override
//    protected int getContentViewId() {
//        return R.layout.activity_position;
//    }
//
//    private void initViews(){
//        editText = (EditText) findViewById(R.id.position_edittext);
//        startbtn = (Button) findViewById(R.id.position_startbtn);
//        startbtn.setOnClickListener(this);
//    }
//
//    private void startGPSservice() {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        /* 判断GPS是否正常启动 */
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            ToastUtils.showShort("请开启GPS导航...");
//            /* 返回开启GPS导航设置界面 */
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivityForResult(intent, 0);
//        }
//
//        /* 获取Location */
//        if (ActivityCompat.checkSelfPermission(GPSPositionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(GPSPositionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ToastUtils.showShort("未获取权限");
//            return;
//        }
//
//        //获取所有可用的位置提供器
//        List<String> providers = locationManager.getProviders(true);
//        if (providers.contains(LocationManager.GPS_PROVIDER)) {
//            //如果是GPS
//            locationProvider = LocationManager.GPS_PROVIDER;
//        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//            //如果是Network
//            locationProvider = LocationManager.NETWORK_PROVIDER;
//        } else {
//            ToastUtils.showShort("没有可用的位置提供器");
//            return;
//        }
//
//        Log.i(TAG, locationProvider);
//
//        /* 为获取地理位置信息时设置查询条件 */
////        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
////
////        Log.i(TAG, "GPS=" + LocationManager.GPS_PROVIDER);
////        Log.i(TAG, "bestProvider=" + bestProvider);
//
//        /* 获取位置信息 */
//        /* 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER */
////        Location location = null;
////        while (location == null) {
////            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////                // TODO: Consider calling
////                //    ActivityCompat#requestPermissions
////                // here to request the missing permissions, and then overriding
////                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                //                                          int[] grantResults)
////                // to handle the case where the user grants the permission. See the documentation
////                // for ActivityCompat#requestPermissions for more details.
////                return;
////            }
////            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////            Log.i(TAG, "long");
////        }
//
//        Location location = locationManager.getLastKnownLocation(locationProvider);
//
////        while(location  == null) {
////            mgr.requestLocationUpdates("gps", 60000, 1, locationListener);
////        }
////
////        if(location!=null) {
////            //定位我的位置
////            MapController controller = mapView.getController();
////            controller.setZoom(16);
////            //latitude 纬度 longitude 经度
////            GeoPoint point =  new GeoPoint((int) (location.getLatitude()*1E6),
////                    (int) (location.getLongitude()*1E6));
////            controller.setCenter(point); //设置地图中心
////            mapView.getOverlays().clear(); //清除地图上所有覆盖物
////            MyLocationOverlay locationOverlay = new MyLocationOverlay(mapView);
////            LocationData locationData = new LocationData();
////            locationData.latitude = location.getLatitude(); //纬度
////            locationData.longitude = location.getLongitude(); //经度
////            locationOverlay.setData(locationData);
////            //添加覆盖物
////            mapView.getOverlays().add(locationOverlay);
////            mapView.refresh(); //刷新
////        }
//
//
//       updateView(location);
//
//        /* 监听状态 */
//        /* 绑定监听，有4个参数
//          参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
//          参数2，位置信息更新周期，单位毫秒
//          参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
//          参数4，监听
//
//          备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
//          1秒更新一次，或最小位移变化超过1米更新一次
//          注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
//          */
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 2, locationListener);
//    }
//
//    /* 位置监听 */
//    private LocationListener locationListener = new LocationListener() {
//
//        /*
//         * 位置信息变化时触发
//         */
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.i(TAG, "onLocationChanged");
//            updateView(location);
//            Log.i(TAG, "时间：" + location.getTime());
//            Log.i(TAG, "经度：" + location.getLongitude());
//            Log.i(TAG, "纬度：" + location.getLatitude());
//            Log.i(TAG, "海拔：" + location.getAltitude());
//        }
//
//        /*
//         * GPS状态变化时触发
//         */
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.i(TAG, "onStatusChanged");
//            switch (status) {
//                //GPS状态为可见时
//                case LocationProvider.AVAILABLE:
//                    Log.i(TAG, "当前GPS状态为可见状态");
//                    break;
//                //GPS状态为服务区外时
//                case LocationProvider.OUT_OF_SERVICE:
//                    Log.i(TAG, "当前GPS状态为服务区外状态");
//                    break;
//                //GPS状态为暂停服务时
//                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    Log.i(TAG, "当前GPS状态为暂停服务状态");
//                    break;
//            }
//        }
//
//        /*
//         * GPS开启时触发
//         */
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.i(TAG, "onproviderEnabled");
//            if (ActivityCompat.checkSelfPermission(GPSPositionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(GPSPositionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            Location location = locationManager.getLastKnownLocation(provider);
//            updateView(location);
//        }
//
//        /*
//         * GPS禁用时触发
//         */
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.i(TAG, "onProviderDisabled");
//            updateView(null);
//        }
//    };
//
//    /*
//     * 实时更新文本内容
//     */
//    private void updateView(Location location){
//        Log.i(TAG,"readyToUpdateView");
//
//        if (location != null){
//            Log.i(TAG,"updateView");
//            editText.setText("设备位置信息:\n\n经度：");
//            editText.append(String.valueOf(location.getLongitude()));
//            editText.append("\n纬度：");
//            editText.append(String.valueOf(location.getLatitude()));
//        } else {
//            // 清空EditText对象
//            Log.i(TAG,"null");
//            editText.getEditableText().clear();
//        }
//    }
//
//    /*
//     * 返回查询条件
//     */
//    private Criteria getCriteria(){
//        Log.i(TAG, "getCriteria()");
//        Criteria criteria = new Criteria();
//        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        //设置是否要求速度
//        criteria.setSpeedRequired(false);
//        // 设置是否允许运营商收费
//        criteria.setCostAllowed(false);
//        //设置是否需要方位信息
//        criteria.setBearingRequired(false);
//        //设置是否需要海拔信息
//         criteria.setAltitudeRequired(false);
//        // 设置对电源的需求
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        return criteria;
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.position_startbtn:
//                startGPSservice();
//                break;
//        }
//    }
//
//}
