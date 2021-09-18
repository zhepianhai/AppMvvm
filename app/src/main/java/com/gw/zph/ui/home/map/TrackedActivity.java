package com.gw.zph.ui.home.map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

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
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.gw.safty.common.utils.NetUtil;
import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.base.db.DbHelper;
import com.gw.zph.base.db.dao.OffLineLatLngInfo;
import com.gw.zph.base.db.dao.OffLineLatLngInfoDao;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.databinding.TrackedActivityBinding;
import com.gw.zph.modle.AllVm;
import com.gw.zph.ui.home.list.MyLetterActivity;
import com.gw.zph.utils.CommonUtils;
import com.gw.zph.utils.Constants;
import com.gw.zph.utils.JSDateUtil;
import com.gw.zph.utils.MapUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gw.zph.core.ConstantKt.DATA;

public class TrackedActivity extends BaseActivityImpl implements AMapLocationListener, DrawerLayout.DrawerListener, SmoothMoveMarker.MoveListener {
    private TrackedActivityBinding binding;
    private static final int REQUIRE_PERMISSION = 1;
    private String persId;

    public AMap mAMap;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private List<OffLineLatLngInfo> list=new ArrayList<>();

    private TrackBottomSheet trackBottomSheet;
    private int enpendHeight;

    private List<LatLng> mListLatlng;
    private float distance;
    private SmoothMoveMarker smoothMarker;
    private boolean mFlagMove;
    private AllVm viewModel;
    public static void openActivity(Context context,String persId) {
        Intent intent = new Intent();
        intent.setClass(context, TrackedActivity.class);
        intent.putExtra(DATA,persId);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_tracked );
        binding.getRoot().setFitsSystemWindows(true);
        viewModel = createViewModel(AllVm.class);
        setupBaseViewModel(viewModel);
        initObserver();
        setupViewAction();
        checkMap(savedInstanceState);
    }

    private void initObserver(){
        if(getIntent()!=null){
            persId=getIntent().getStringExtra(DATA);
        }
        enpendHeight=CommonUtils.getWindowHeight(this)/2;
        binding.llBack.setOnClickListener(v->{
            TrackedActivity.this.finish();
        });
        binding.btnFa.setOnClickListener(v->{
            smoothMarker();
        });
        binding.llRight.setOnClickListener(v->{
            if(trackBottomSheet==null){
                trackBottomSheet=new TrackBottomSheet(list);
            }
            trackBottomSheet.setTopOffset(enpendHeight);
            trackBottomSheet.show(getSupportFragmentManager(),"tag");
        });
    };

    private void setupViewAction(){
        viewModel.getTrackListModel().observe(this,items->{
            list.clear();
            list.addAll(items);
            show();
        });



    };
    private void showTracked(){
        mListLatlng=new ArrayList<>();
        mListLatlng.clear();
        if(StatusHolder.INSTANCE.getUserBean()==null) {
            List<OffLineLatLngInfo> items = DbHelper.getInstance().offLineLatLngInfoLocDBManager().queryBuilder()
                    .where(OffLineLatLngInfoDao.Properties.Mobile.eq(persId))
                    .list();
            if(items!=null){
                list.addAll(items);
            }
            show();

        }else{
            viewModel.getTrackedByPhoneList(persId);
        }

    }
    private void show(){
        for (OffLineLatLngInfo item : list) {
            LatLng latLng = new LatLng(JSDateUtil.getDatadoubleByObj(item.getLat()), JSDateUtil.getDatadoubleByObj(item.getLon()));
            mListLatlng.add(latLng);
        }
        MapUtils.addTracker(mAMap, mListLatlng, this);
    }
    private void smoothMarker(){
        if(mListLatlng==null) return;
        if(smoothMarker==null){
            mFlagMove=true;
        }
        else{
            mFlagMove=true;
            smoothMarker.stopMove();
            smoothMarker.removeMarker();
            smoothMarker.destroy();
        }
       if(mFlagMove) {
           mFlagMove=true;
           LatLngBounds bounds;
           if (mListLatlng.size() <= 2) {
               bounds = new LatLngBounds(mListLatlng.get(0), mListLatlng.get(0));
           } else {
               bounds = new LatLngBounds(mListLatlng.get(0), mListLatlng.get(mListLatlng.size() - 2));
           }
//           if(smoothMarker==null) {
               smoothMarker = new SmoothMoveMarker(mAMap);
//           }
           LatLng drivePoint = mListLatlng.get(0);
           Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(mListLatlng, drivePoint);
           mListLatlng.set(pair.first, drivePoint);
           List<LatLng> subList = mListLatlng.subList(pair.first, mListLatlng.size());
           // 设置滑动的轨迹左边点
           smoothMarker.setPoints(subList);
           // 设置滑动的总时间
           int sp = 10;
           if (distance != 0) {
               sp = (int) (distance / 20);
           }
           if (sp > 25) {
               sp = 25;
           }
           smoothMarker.setTotalDuration(sp);
           // 开始滑动
           smoothMarker.startSmoothMove();
           smoothMarker.setMoveListener(this);
       }else{
           if(smoothMarker!=null) {
               smoothMarker.stopMove();
               smoothMarker.removeMarker();
               smoothMarker.destroy();
               mFlagMove = false;
           }
       }
    }
    private void checkMap(Bundle savedInstanceState){
        if(hasExternalStoragePermission()){
            initMap(savedInstanceState);
        }else{
            getFilePermission();
        }
    }
    private boolean hasExternalStoragePermission() {
        int perm = checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION");
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private void getFilePermission() {
        ArrayList<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUIRE_PERMISSION);
            }
        }
    }

    private void initMap(Bundle savedInstanceState) {
        binding.trackMap.onCreate(savedInstanceState);
        mAMap=binding.trackMap.getMap();
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_loc));
        myLocationStyle.strokeColor(Color.argb(40, 177, 225, 248));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(40, 177, 225, 248));// 设置圆形的填充颜色
        myLocationStyle.anchor(0.5f,0.5f); // 这个数值是根据我的图片显示的时候计算的，最好是0.5 0.5
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
    private void setUpLocation(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        if (NetUtil.hasNetwork(this)) {
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
        showTracked();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0&&aMapLocation!=null) {
            LatLng pt = new LatLng(
                    aMapLocation.getLatitude()
                    , aMapLocation.getLongitude()
                    , false);
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(
                            pt
                            , Constants.MAP_SCALE_BIG, 0, 0));
            mAMap.moveCamera(mCameraUpdate);//缩放地图到指定的缩放级别
        }else {
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        binding.trackMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.trackMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.trackMap.onDestroy();
    }

    @Override
    public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull @NotNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull @NotNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void move(double v) {
        if(v==0){
            mFlagMove=false;
            smoothMarker.stopMove();
            smoothMarker.removeMarker();
            smoothMarker.destroy();
        }
    }

    public void refreshCurTexture(float distance, List<LatLng> latLngList) {
        this.runOnUiThread(() -> {
            this.distance = distance;
            float dis = (float) (Math.round(distance * 100) / 100);
            if (dis > 1000.0f) {
                binding.tvDistance.setText("里程:" + dis / 1000.0f + "km");
            } else {
                binding.tvDistance.setText("里程:" + dis + "m");
            }
        });
    }
}