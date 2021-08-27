package com.gw.zph.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class MainActivity extends BaseActivityImpl {
    private MainActBinding binding;

    private static final int defaultSelectTabIndex = 0; //默认选中的tab
    private TabLayoutMediator tabMediator; //工具类，绑定tabLayout和ViewPager
    private ArrayList<View> tabViews;//展示的所有tab使用的自定义view
    private ArrayList<Fragment> fragments; //展示的所有fragment
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main );
        binding.setLifecycleOwner(this);
        viewModel = createViewModel(MainActivityViewModel.class);
        setupTabs();
        setupFragments(binding.mainPager);
        TabLayout.Tab selectedTab = binding.mainNav.getTabAt(defaultSelectTabIndex);
        if (selectedTab != null) selectedTab.select();
    }
    private void setupTabs() {
        tabViews = new ArrayList<>();
        tabViews.add(createCustomTabView("首页", R.drawable.main_homepage_drawable));
        tabViews.add(createCustomTabView("定位", R.drawable.main_yzt_drawable));
        tabViews.add(createCustomTabView("感谢信", R.drawable.main_list_drawable));
        tabViews.add(createCustomTabView("我的", R.drawable.main_mine_drawable));
    }

    private void setupFragments(ViewPager2 pager){
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MapFragment());
        fragments.add(new ProListFragment());
        fragments.add(new MeFragment());

        if (tabViews.size() != fragments.size()){
            ActivityExtensionsKt.exShortToast(this, "Fragment与Tab数目不对");
            return;
        }
        pager.setOffscreenPageLimit(4);
        pager.setUserInputEnabled(false);
        pager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        if (tabMediator != null) {
            tabMediator.detach();
        }
        tabMediator = new TabLayoutMediator(binding.mainNav, pager, (tab, position) -> {
            tab.setCustomView(tabViews.get(position));
        });
        tabMediator.attach();
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
}