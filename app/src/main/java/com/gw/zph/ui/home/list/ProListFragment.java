package com.gw.zph.ui.home.list;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.View;

import com.gw.slbdc.ui.main.mine.HomeFragmentViewModel;
import com.gw.zph.R;
import com.gw.zph.base.BaseFragmentImpl;
import com.gw.zph.databinding.ProListFragmentBinding;
import com.gw.zph.base.db.dao.ProBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;


public class ProListFragment extends BaseFragmentImpl<ProListFragmentBinding> implements AdpProList.ProItemClickListener {
    private static final int REQUIRE_PERMISSION = 1;
    private HomeFragmentViewModel viewModel;
    private ProListFragmentBinding binder;

    private AdpProList adpProList;
    private List<ProBean> list;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_pro_list;
    }
    public ProListFragment() {
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = createViewModel(HomeFragmentViewModel.class);
        setupViewAction();
        setupViewModel();
        setupFunction();
        initRecycle();
    }
    private void setupFunction() {
        binder.refresh.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent,R.color.yellow,R.color.declaration_item_status_a);
        binder.refresh.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> binder.refresh.setRefreshing(false),3000);
        });

    }
    private void setupViewModel() {
        setupBaseViewModel(viewModel);
    }

    private void setupViewAction() {
        binder = Objects.requireNonNull(getBinder());
        binder.lay0.setOnClickListener(v->{
            LetterEditActivity.openActivity(getContext());
        });
        binder.lay1.setOnClickListener(v->{
            MyLetterActivity.openActivity(getContext());
        });
    }

    private void initRecycle(){
        list=new ArrayList<>();
        initVariData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adpProList=new AdpProList(getContext(),list);
        binder.recycleView.setAdapter(adpProList);
        binder.recycleView.setLayoutManager(linearLayoutManager);
        adpProList.setOnItemClickListener(this);
    }


    @Override
    public void onVillProjectClickListener(ProBean item) {
        //Item 点击
    }

    private void initVariData(){
        ProBean proBean=new ProBean();
        proBean.setContent("你们好，我今天终于找到了我一直在找的朋友！我这位朋友打电话不接，发短信也不回，通过这个APP,我一下子就定位到了我朋友，十分感谢...");
        proBean.setPer("186****8800");
        proBean.setTime("2021年8月22日15:22");
        list.add(proBean);

        ProBean proBean1=new ProBean();
        proBean1.setContent("太感谢了，这款软件太厉害了，只要输入手机号就的查到他的详细位置，他的活动轨全被我知道了，这下的看他还想么说出去");
        proBean1.setPer("139****2191");
        proBean1.setTime("2021年8月21日17:16");
        list.add(proBean1);

        ProBean proBean2=new ProBean();
        proBean2.setContent("我那大马哈的13岁小儿子今天出去玩，把手机调成了静音。一直联系不上，吓死我了，多亏了这个软件，我在定位附近一下就发现他了");
        proBean2.setPer("138****1168");
        proBean2.setTime("2021年8月22日12:23");
        list.add(proBean2);
    }
}