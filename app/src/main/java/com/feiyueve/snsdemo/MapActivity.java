package com.feiyueve.snsdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.feiyueve.snsdemo.permission.PermissionsUtilX;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapActivity extends AppCompatActivity implements LocationSource, AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener,
        AMap.OnMapClickListener {

    private MapView mapView;
    private AMap aMap;
    private UiSettings uiSettings;
    //定位服务
    private LocationSource.OnLocationChangedListener onLocationChangedListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationClientOption;
    //地理编码
    private GeocodeSearch geocodeSearch;
    //回显位置信息的TextView
    private TextView locationCoordinate;
    private TextView locationInfo;
    //当前地图上的marker
    private Marker marker;
    private MyApplication myApplication;
    private String locationStr;
    PermissionsUtilX permissionsUtilX;
    private Button buttonGetLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        myApplication = (MyApplication) this.getApplication();
        buttonGetLocation = (Button)findViewById(R.id.buttonGetLocation);
        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        locationCoordinate = findViewById(R.id.location_coordinate);
        locationInfo = findViewById(R.id.location_info);
        getPermission();
        if (aMap == null) {
            aMap = mapView.getMap();
            uiSettings = aMap.getUiSettings();
            //设置地图属性
            setMapAttribute();
        }

        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(locationStr);
                myApplication.getPersonalInf().setLocation(locationStr);
                Intent intent = new Intent();
                intent.setClass(MapActivity.this, UpdateUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getPermission() {
        String permissions[] = {PermissionsUtilX.Permission.Location.ACCESS_COARSE_LOCATION,PermissionsUtilX.Permission.Location.ACCESS_FINE_LOCATION};
        permissionsUtilX = PermissionsUtilX.with(this)
                .requestCode(0)
                .isDebug(true)
                .permissions(permissions)
                .request();

    }

    private void setMapAttribute() {
        //设置默认缩放级别
        aMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //隐藏的右下角缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        //显示右上角定位按钮
        uiSettings.setMyLocationButtonEnabled(false);
        //设置定位监听
        aMap.setLocationSource(this);
        //可触发定位并显示当前位置
        aMap.setMyLocationEnabled(true);
        //定位一次，且将视角移动到地图中心点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //隐藏定位点外圈圆的颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        aMap.setMyLocationStyle(myLocationStyle);
        //设置地理编码查询
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        //设置地图点击事件
        aMap.setOnMapClickListener(this);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
        if (locationClient == null) {
            //初始化定位
            locationClient = new AMapLocationClient(this);
            //初始化定位参数
            locationClientOption = new AMapLocationClientOption();
            //设置定位回调监听
            locationClient.setLocationListener(this);
            //高精度定位模式
            locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //单定位模式
            locationClientOption.setOnceLocation(true);
            //设置定位参数
            locationClient.setLocationOption(locationClientOption);
            //启动定位
            locationClient.startLocation();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (onLocationChangedListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //显示定位圆点
                onLocationChangedListener.onLocationChanged(aMapLocation);
                locationCoordinate.setText("当前纬度:" + aMapLocation.getLatitude() + "当前经度" + aMapLocation.getLongitude());
                //根据当前经纬度查询地址
                LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(query);
            } else {
                Log.e("YyyyQ", "定位失败" + aMapLocation.getErrorCode() + ":" + aMapLocation.getErrorInfo());
                Toast.makeText(getApplication(), "定位失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        onLocationChangedListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
    }

    /**
     * 根据坐标转换地址信息
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            locationStr = regeocodeResult.getRegeocodeAddress().getFormatAddress();
            locationInfo.setText("当前位置信息:" + locationStr);
        } else {
            Toast.makeText(getApplication(), "获取当前位置信息失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 地址转坐标
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 地图点击事件
     */
    @Override
    public void onMapClick(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon));
        markerOptions.position(latLng);
        marker = aMap.addMarker(markerOptions);
        //根据点击地图的点位获取详细信息
        locationCoordinate.setText("当前纬度:" + latLng.latitude + "当前经度" + latLng.longitude);
        //根据当前经纬度查询地址
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }

}