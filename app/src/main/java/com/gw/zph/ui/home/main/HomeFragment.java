package com.gw.zph.ui.home.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.gw.safty.common.utils.NetUtil;
import com.gw.safty.common.utils.ToastUtil;
import com.gw.slbdc.ui.main.mine.HomeFragmentViewModel;
import com.gw.zph.R;
import com.gw.zph.base.BaseFragmentImpl;
import com.gw.zph.base.db.DbHelper;
import com.gw.zph.base.db.dao.OffLineLatLngInfo;
import com.gw.zph.base.db.dao.OffLineLatLngInfoDao;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.databinding.HomeFragmentBinding;
import com.gw.zph.model.login.bean.UserBean;
import com.gw.zph.modle.AllVm;
import com.gw.zph.ui.home.map.TrackedActivity;
import com.gw.zph.utils.Constants;
import com.gw.zph.utils.JSDateUtil;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;


public class HomeFragment extends BaseFragmentImpl<HomeFragmentBinding> implements AMapLocationListener {
    private static final int REQUIRE_PERMISSION = 1;
    private AllVm viewModel;
    private HomeFragmentBinding binder;
    public SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public UserBean userBean;

    public AMapLocation mamapl = null;
    public AMap mAMap;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = createViewModel(AllVm.class);
        setupViewAction();
        setupViewModel();
        setupFunction();
    }

    @Override
    public void onResume() {
        super.onResume();
        userBean = StatusHolder.INSTANCE.getCurUserBean(getContext());
        showInfo();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    private void setupFunction() {
        binder.refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.yellow, R.color.declaration_item_status_a);
        binder.refresh.setOnRefreshListener(() -> {
            showInfo();
            new Handler().postDelayed(() -> binder.refresh.setRefreshing(false), 3000);
        });
        binder.btnLocation.setOnClickListener(v -> {
            AddPosActivity.openActivity(getContext());
        });
        binder.btnTrack.setOnClickListener(v -> {
            if (StatusHolder.INSTANCE.getCurUserBean(getContext()) == null) {
                ToastUtil.showToast(getContext(), "请登录！");
                return;
            }
            TrackedActivity.openActivity(getContext(), StatusHolder.INSTANCE.getCurUserBean(getContext()).getPhone());
        });
        viewModel.getTrackListModel().observe(getViewLifecycleOwner(),list->{
            if(list!=null&&list.size()>0){
                OffLineLatLngInfo offLineLatLngInfo = list.get(list.size() - 1);
                binder.tvLocation.setText(offLineLatLngInfo.getAddress());
                binder.tvTime.setText(offLineLatLngInfo.getOperateTime());
            }else{
                setUpLocation();
            }
        });
    }

    private void setupViewModel() {
        setupBaseViewModel(viewModel);
    }

    private void setupViewAction() {
        binder = Objects.requireNonNull(getBinder());
        userBean = StatusHolder.INSTANCE.getCurUserBean(getContext());
    }

    private void showInfo() {
        if (userBean == null) {
            List<OffLineLatLngInfo> item = DbHelper.getInstance().offLineLatLngInfoLocDBManager().queryBuilder()
                    .where(OffLineLatLngInfoDao.Properties.PersId.eq("100"))
                    .list();
            if (item != null && item.size() > 0) {
                OffLineLatLngInfo its = item.get(item.size() - 1);
                binder.tvLocation.setText(its.getAddress());
                binder.tvTime.setText(its.getOperateTime());
            } else {
//                binder.tvLocation.setText("暂无");
//                binder.tvTime.setText("暂无");
                setUpLocation();
            }

        }else{
            viewModel.getTrackedByPhoneList(userBean.getPhone());
        }
    }
    private void setUpLocation(){
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
        int perm = this.getActivity().checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private void getFilePermission() {
        ArrayList<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (this.getActivity().checkSelfPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
            }

            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUIRE_PERMISSION);
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null&&aMapLocation.getErrorCode() == 0) {
            mamapl = aMapLocation.clone();
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
        }else{
            binder.tvLocation.setText("定位失败！");
            binder.tvTime.setText(sdf1.format(new Date()));
        }
    }
}