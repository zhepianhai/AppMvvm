package com.gw.zph.ui.home.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gw.safty.common.utils.ToastUtil;
import com.gw.slbdc.ui.main.mine.HomeFragmentViewModel;
import com.gw.zph.R;
import com.gw.zph.base.BaseFragmentImpl;
import com.gw.zph.base.db.DbHelper;
import com.gw.zph.base.db.dao.OffLineLatLngInfo;
import com.gw.zph.base.db.dao.OffLineLatLngInfoDao;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.databinding.HomeFragmentBinding;
import com.gw.zph.ui.home.map.TrackedActivity;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;


public class HomeFragment extends BaseFragmentImpl<HomeFragmentBinding> {
    private static final int REQUIRE_PERMISSION = 1;
    private HomeFragmentViewModel viewModel;
    private HomeFragmentBinding binder;
    public SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = createViewModel(HomeFragmentViewModel.class);
        setupViewAction();
        setupViewModel();
        setupFunction();
    }

    @Override
    public void onResume() {
        super.onResume();
        showInfo();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    private void setupFunction() {
        binder.refresh.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent,R.color.yellow,R.color.declaration_item_status_a);
        binder.refresh.setOnRefreshListener(() -> {
            showInfo();
            new Handler().postDelayed(() -> binder.refresh.setRefreshing(false),3000);
        });
        binder.btnLocation.setOnClickListener(v->{
            AddPosActivity.openActivity(getContext());
        });
        binder.btnTrack.setOnClickListener(v->{
            if(StatusHolder.INSTANCE.getCurUserBean(getContext())==null){
                ToastUtil.showToast(getContext(),"请登录！");
                return;
            }
            TrackedActivity.openActivity(getContext(),StatusHolder.INSTANCE.getCurUserBean(getContext()).getPhone());
        });
    }

    private void setupViewModel() {
        setupBaseViewModel(viewModel);
    }

    private void setupViewAction() {
        binder = Objects.requireNonNull(getBinder());
    }
    private void showInfo(){
        List<OffLineLatLngInfo> item = DbHelper.getInstance().offLineLatLngInfoLocDBManager().queryBuilder()
                .where(OffLineLatLngInfoDao.Properties.PersId.eq("100"))
                .list();
        if(item!=null&&item.size()>0){
            OffLineLatLngInfo its=item.get(item.size()-1);
            binder.tvLocation.setText(its.getAddress());
            binder.tvTime.setText(its.getOperateTime());
        }else{
            binder.tvLocation.setText("暂无");
            binder.tvTime.setText("暂无");
        }
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
}