package com.gw.zph.ui.home.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.gw.safty.common.utils.MyNotificationManagerUtil;
import com.gw.safty.common.utils.NetUtil;
import com.gw.safty.common.utils.ServiceUtil;
import com.gw.slbdc.ui.main.mine.HomeFragmentViewModel;
import com.gw.zph.R;
import com.gw.zph.application.MyApplication;
import com.gw.zph.base.BaseFragmentImpl;
import com.gw.zph.base.db.DbHelper;
import com.gw.zph.base.db.dao.OffLineLatLngInfo;
import com.gw.zph.base.db.dao.OffLineLatLngInfoDao;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.databinding.HomeFragmentBinding;
import com.gw.zph.databinding.MapFragmentBinding;
import com.gw.zph.model.login.bean.UserBean;
import com.gw.zph.service.KeepLiveService;
import com.gw.zph.service.LocationService;
import com.gw.zph.ui.home.main.AddPosActivity;
import com.gw.zph.utils.Constants;
import com.gw.zph.utils.JSDateUtil;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;


public class MapFragment extends BaseFragmentImpl<MapFragmentBinding> implements AMapLocationListener {
    private static final int REQUIRE_PERMISSION = 1;
    private MapViewModel viewModel;
    private MapFragmentBinding binder;


    MyApplication application;
    UserBean userBean;
    public SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public AMapLocation mamapl = null;
    public AMap mAMap;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    public MapFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = createViewModel(MapViewModel.class);
        setupViewAction();
        setupViewModel();
        setupFunction();
        checkMap(savedInstanceState);
    }

    private void setupViewModel() {
        setupBaseViewModel(viewModel);
    }

    private void setupViewAction() {
        application = (MyApplication) getActivity().getApplication();
        binder = Objects.requireNonNull(getBinder());
        userBean = StatusHolder.INSTANCE.getLoginUserBean();
    }

    private void setupFunction() {
        binder.btnLoc.setOnClickListener(v -> {
            AddPosActivity.openActivity(getContext());
        });
        binder.btnTrack.setOnClickListener(v -> {
            TrackedActivity.openActivity(getContext(), "100");
        });
        binder.imgLoc.setOnClickListener(v -> {
            if (mLocationClient != null) {
                mLocationClient.stopLocation();
                mLocationClient.startLocation();
            }
        });
    }

    private void checkMap(Bundle savedInstanceState) {
        if (hasExternalStoragePermission()) {
            initMap(savedInstanceState);
        } else {
            getFilePermission();
        }
    }

    private void initMap(Bundle savedInstanceState) {
        binder.map.onCreate(savedInstanceState);
        mAMap = binder.map.getMap();
        MyLocationStyle myLocationStyle;

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_loc));
        myLocationStyle.strokeColor(Color.argb(40, 177, 225, 248));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(40, 177, 225, 248));// 设置圆形的填充颜色
        myLocationStyle.anchor(0.5f, 0.5f); // 这个数值是根据我的图片显示的时候计算的，最好是0.5 0.5
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);

        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mAMap.setMyLocationEnabled(true);

        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(37.504838, 104.464987), 3.6f, 0, 0));
        mAMap.moveCamera(mCameraUpdate);//缩放地图到指定的缩放级别
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        uiSettings.setZoomControlsEnabled(false);//设置缩放按钮不可用
        uiSettings.setTiltGesturesEnabled(false);//设置地图不可倾斜
        uiSettings.setRotateGesturesEnabled(false);//设置地图是不可旋转
        uiSettings.setLogoBottomMargin(-38);//隐藏logo
        uiSettings.setScaleControlsEnabled(false);//控制比例尺控件是否显示

        setUpLocation();
    }

    private void setUpLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext().getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        if (NetUtil.hasNetwork(getActivity())) {
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        } else {
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        }
        mLocationOption.setOnceLocation(true);
        mLocationOption.setLocationCacheEnable(false);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationClient.setLocationOption(option);
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient.stopLocation();
        mLocationClient.startLocation();
    }

    private boolean hasExternalStoragePermission() {
        int perm = this.getActivity().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION");
        return perm == PackageManager.PERMISSION_GRANTED;

    }

    private void getFilePermission() {
        ArrayList<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUIRE_PERMISSION);
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (userBean != null) {
            if (!ServiceUtil.isServiceRunning(getContext(), "com.gw.zph.service.service.LocationService")) {
                getActivity().startService(new Intent(getActivity(), LocationService.class));
                getActivity().startService(new Intent(getActivity(), KeepLiveService.class));
            }
//        } else {
//            MyNotificationManagerUtil myNotificationManagerUtil = new MyNotificationManagerUtil(getContext());
//            myNotificationManagerUtil.clearNotification();
//
//        }
        mamapl = aMapLocation.clone();
        MyApplication application = (MyApplication) getActivity().getApplication();
        application.setMamapl(mamapl);
        if (aMapLocation.getErrorCode() == 0) {
            LatLng pt = new LatLng(
                    mamapl.getLatitude()
                    , mamapl.getLongitude()
                    , false);
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(
                            pt
                            , Constants.MAP_SCALE_BIG, 0, 0));
            mAMap.moveCamera(mCameraUpdate);//缩放地图到指定的缩放级别
            binder.tvTime.setText(sdf1.format(new Date()));
            binder.tvLocation.setText(JSDateUtil.getDataStringByObj(mamapl.getAddress()));

            OffLineLatLngInfo info = new OffLineLatLngInfo();
            info.setAdCode(mamapl.getAdCode());
            info.setPersId("100");
            info.setPersName("我");
            info.setLat(mamapl.getLatitude());
            info.setLon(mamapl.getLongitude());
            info.setOperateTime(sdf1.format(new Date().getTime()));
            info.setMobile("");
            info.setAddress(JSDateUtil.getDataStringByObj(mamapl.getAddress()));
            info.setSpeed(mamapl.getSpeed());
            DbHelper.getInstance().offLineLatLngInfoLocDBManager().insert(info);

        } else {
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binder.map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binder.map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binder.map.onDestroy();
    }
}