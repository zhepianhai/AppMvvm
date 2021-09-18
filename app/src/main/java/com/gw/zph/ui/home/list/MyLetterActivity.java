package com.gw.zph.ui.home.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.base.db.DbHelper;
import com.gw.zph.base.db.dao.OffLineLatLngInfo;
import com.gw.zph.base.db.dao.OffLineLatLngInfoDao;
import com.gw.zph.base.db.dao.ProBean;
import com.gw.zph.base.db.dao.ProBeanDao;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.databinding.LetterEditActivityBinding;
import com.gw.zph.databinding.MyLetterActivityBinding;

import java.util.ArrayList;
import java.util.List;

public class MyLetterActivity extends BaseActivityImpl implements AdpProList.ProItemClickListener {
    private MyLetterActivityBinding binding;
    private AdpProList adpProList;
    private List<ProBean> list;

    public static void openActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyLetterActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_letter );
        binding.getRoot().setFitsSystemWindows(true);
        initObserver();
        setupViewAction();
    }
    private void initObserver(){
        binding.llBack.setOnClickListener(v->{
            this.finish();
        });
        list=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adpProList=new AdpProList(this,list);
        binding.recycleView.setAdapter(adpProList);
        binding.recycleView.setLayoutManager(linearLayoutManager);
        adpProList.setOnItemClickListener(this);
    };
    private void setupViewAction(){
        List<ProBean> item = DbHelper.getInstance().getProBeanDaoLongDBManager().queryBuilder()
                .where(ProBeanDao.Properties.PersId.eq(StatusHolder.INSTANCE.getCurUserBean(this).getPhone()))
                .list();

        if(item==null||item.size()==0){
            binding.recycleView.setVisibility(View.INVISIBLE);
            binding.layEmpty.setVisibility(View.VISIBLE);
        }else{
            binding.recycleView.setVisibility(View.VISIBLE);
            binding.layEmpty.setVisibility(View.INVISIBLE);
            list.clear();
            list.addAll(item);
            adpProList.setList(list);

        }

    };

    @Override
    public void onVillProjectClickListener(ProBean item) {

    }
}