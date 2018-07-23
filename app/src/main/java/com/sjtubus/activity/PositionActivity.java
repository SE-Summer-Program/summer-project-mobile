package com.sjtubus.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.sjtubus.R;
import com.sjtubus.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class PositionActivity extends BaseActivity implements View.OnClickListener{

    private LocationClient locationClient;
    private EditText editText;
    private Button startbtn;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        locationClient.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_position;
    }


    private void initView(){

        editText = (EditText) findViewById(R.id.position_edittext);
        startbtn = (Button) findViewById(R.id.position_startbtn);
        startbtn.setOnClickListener(this);

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());

        editText = (EditText) findViewById(R.id.position_edittext);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(PositionActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add( Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(PositionActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add( Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(PositionActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add( Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(PositionActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation(){
        initLocation();
        locationClient.start();
        ToastUtils.showShort("Get Location");
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(500);
        locationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            ToastUtils.showShort("必须同意所有权限才可以使用哦~");
                            finish();
                            return;
                        }
                    }
                    requestLocation();;
                } else {
                    ToastUtils.showShort("发生未知错误！");
                    finish();
                }
                break;
            default:
                break;
        }
    }

    public class MyLocationListener  implements BDLocationListener{

        @Override
        public void onReceiveLocation(final BDLocation location){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPosition = new StringBuilder();
                    currentPosition.append("纬度: ").append(location.getLatitude()).append("\n");
                    currentPosition.append("经度: ").append(location.getLongitude()).append("\n");
                    currentPosition.append("定位方式: ");
                    if (location.getLocType() == BDLocation.TypeGpsLocation){
                        currentPosition.append("GPS");
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                        currentPosition.append("网络");
                    }
                    editText.setText(currentPosition);
                }
            });
        }

//        @Override
//        public void onConnectHotSpotMessage(String s, int i){
//
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.position_startbtn:

                break;
            default:
                break;
        }
    }
}
