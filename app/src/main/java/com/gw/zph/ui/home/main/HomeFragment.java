package com.gw.zph.ui.home.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gw.slbdc.ui.main.mine.HomeFragmentViewModel;
import com.gw.zph.R;
import com.gw.zph.base.BaseFragmentImpl;
import com.gw.zph.databinding.HomeFragmentBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;


public class HomeFragment extends BaseFragmentImpl<HomeFragmentBinding> {
    private static final int REQUIRE_PERMISSION = 1;
    private HomeFragmentViewModel viewModel;
    private HomeFragmentBinding binder;

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = createViewModel(HomeFragmentViewModel.class);
        setupViewAction();
        setupViewModel();
        setupFunction();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    private void setupFunction() {
        binder.refresh.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent,R.color.yellow,R.color.declaration_item_status_a);
        binder.refresh.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> binder.refresh.setRefreshing(false),3000);
        });
        binder.btnLocation.setOnClickListener(v->{
            AddPosActivity.openActivity(getContext());
        });
    }

    private void setupViewModel() {
        setupBaseViewModel(viewModel);
    }

    private void setupViewAction() {
        binder = Objects.requireNonNull(getBinder());
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