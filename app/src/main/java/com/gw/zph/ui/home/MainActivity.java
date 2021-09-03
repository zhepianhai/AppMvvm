package com.gw.zph.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gw.safty.common.extensions.ActivityExtensionsKt;
import com.gw.slbdc.ui.main.MainActivityViewModel;
import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.databinding.MainActBinding;
import com.gw.zph.ui.home.list.ProListFragment;
import com.gw.zph.ui.home.main.HomeFragment;
import com.gw.zph.ui.home.map.MapFragment;
import com.gw.zph.ui.home.me.MeFragment;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;

import static android.view.KeyEvent.KEYCODE_BACK;

public class MainActivity extends BaseActivityImpl implements TabChangeListener {
    private MainActBinding binding;
    private MainFrameTabManager mTabManager;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        binding.getRoot().setFitsSystemWindows(true);
        viewModel = createViewModel(MainActivityViewModel.class);
        init();
        //默认选中首页
        mTabManager.onTabChanged("首页");
        binding.mainTabHost.setCurrentTab(0);
        binding.mainTabHost.invalidate();
    }

    private void init() {
        binding.mainTabHost.setup();
        mTabManager = new MainFrameTabManager(this, binding.mainTabHost,
                R.id.tab_real_content);
        mTabManager.setTabChangeListener(this);
        Bundle bundle = new Bundle();
        Class<?> homeFragmentClass = HomeFragment.class;
        Class<?> mapFragmentClass = MapFragment.class;
        Class<?> proListFragmentClass = ProListFragment.class;
        Class<?> meFragmentClass = MeFragment.class;

        mTabManager.addTab(
                binding.mainTabHost.newTabSpec("首页").setIndicator(
                        createCustomTabView("首页", R.drawable.main_homepage_drawable)), homeFragmentClass, bundle);
        mTabManager.addTab(
                binding.mainTabHost.newTabSpec("定位").setIndicator(
                        createCustomTabView("定位", R.drawable.main_yzt_drawable)), mapFragmentClass, bundle);

        mTabManager.addTab(
                binding.mainTabHost.newTabSpec("感谢信").setIndicator(
                        createCustomTabView("感谢信", R.drawable.main_list_drawable)), proListFragmentClass, bundle);

        mTabManager.addTab(
                binding.mainTabHost.newTabSpec("我的").setIndicator(
                        createCustomTabView("我的", R.drawable.main_mine_drawable)), meFragmentClass, bundle);

    }


    private View createCustomTabView(String txt, int id) {
        LinearLayout linearLayout = new LinearLayout(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_main_tab, linearLayout);
        ImageView tabImg = view.findViewById(R.id.ivMainTabIcon);
        tabImg.setImageResource(id);
        TextView tabTxt = view.findViewById(R.id.tvMainTabLabel);
        tabTxt.setText(txt);
        return view;
    }

    @Override
    public void tabChange(String tabId) {
        Fragment currentFrag = mTabManager.getCurrentFrag();

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    public boolean
    onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK) {
            Fragment currentFrag = mTabManager.getCurrentFrag();
            if (currentFrag != null) {
                if (currentFrag instanceof HomeFragment) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("提示" );
                    builder.setMessage("是否退出应用？");

                    builder.setPositiveButton("确定", (arg0, arg1) -> {
                        // TODO Auto-generated method stub
                        finish();
                        arg0.dismiss();
                    });
                    builder.setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss());
                    builder.create().show();
                }else{
                    mTabManager.onTabChanged("首页");
                    binding.mainTabHost.setCurrentTab(0);
                    binding.mainTabHost.invalidate();
                }
            }
        }
        return false;
    }
}