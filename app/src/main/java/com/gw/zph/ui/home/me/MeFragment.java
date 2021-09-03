package com.gw.zph.ui.home.me;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.gw.slbdc.ui.main.mine.HomeFragmentViewModel;
import com.gw.zph.R;
import com.gw.zph.base.BaseFragmentImpl;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.databinding.MeFragmentBinding;
import com.gw.zph.ui.home.main.AddPosActivity;
import com.gw.zph.ui.login.LoginActivity;
import com.gw.zph.utils.JSDateUtil;
import com.gw.zph.view.LoginTipDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

import javax.annotation.Nullable;

public class MeFragment extends BaseFragmentImpl<MeFragmentBinding> {

    private static final int REQUIRE_PERMISSION = 1;
    private HomeFragmentViewModel viewModel;
    private MeFragmentBinding binder;

    public MeFragment() {
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

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
        if(StatusHolder.INSTANCE.getLoginUserBean()==null) {
            double r1 = Math.random();
            double r2 = Math.random();
            if (r1 > 0.5 && r2 > 0.5) {
                showLogin();
            }
        }
    }

    private void setupViewModel() {
        setupBaseViewModel(viewModel);
    }

    private void setupViewAction() {
        binder = Objects.requireNonNull(getBinder());
    }

    private void setupFunction() {
        if (StatusHolder.INSTANCE.getLoginUserBean() == null) {
            binder.btnLogin.setText("登录");
            binder.tvNPhone.setText("");
        } else {
            binder.btnLogin.setText("+添加想定位的人");
            binder.tvNPhone.setText("131****5893");
        }



        try {
            binder.tvVersion.setText(JSDateUtil.getDataStringByObj(getVersionName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        binder.btnLogin.setOnClickListener(v -> {
            if (StatusHolder.INSTANCE.getLoginUserBean() == null) {
                LoginActivity.openActivity(getContext());
            } else {
                AddPosActivity.openActivity(getContext());
            }
        });
        binder.layShare.setOnClickListener(v -> {
            //分享

        });
        binder.layKefu.setOnClickListener(v -> {
            //客服
        });
        binder.layYHXY.setOnClickListener(v -> {
            //用户协议
        });
        binder.layYS.setOnClickListener(v -> {
            //隐私
        });
    }
    private void showLogin(){
        LoginTipDialog  dialog=new LoginTipDialog(getContext());
        dialog.show();
    }
    private String getVersionName() throws Exception {

        // 获取packagemanager的实例
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }
}