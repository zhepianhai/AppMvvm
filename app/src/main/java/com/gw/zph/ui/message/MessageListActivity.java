package com.gw.zph.ui.message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gw.safty.common.utils.ToastUtil;
import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.databinding.MessageListActivityBinding;
import com.gw.zph.databinding.MyLetterActivityBinding;
import com.gw.zph.modle.AllVm;
import com.gw.zph.modle.bean.MsgBean;
import com.gw.zph.ui.home.list.AdpProList;
import com.gw.zph.ui.home.list.MyLetterActivity;
import com.gw.zph.ui.message.adapter.MsgAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends BaseActivityImpl implements MsgAdapter.ProItemClickListener {
    private MessageListActivityBinding binding;
    private List<MsgBean> list;
    private MsgAdapter adpProList;
    private AllVm viewModel;
    public static void openActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MessageListActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_message_list );
        binding.getRoot().setFitsSystemWindows(true);
        viewModel = createViewModel(AllVm.class);
        setupBaseViewModel(viewModel);
        initObserver();
        setupViewAction();
    }
    private void initObserver(){
        binding.llBack.setOnClickListener(v->{
            this.finish();
        });
        binding.tvMsg.setOnClickListener(v->{
            if(StatusHolder.INSTANCE.getCurUserBean(this)==null){
                ToastUtil.showToast(MessageListActivity.this,"请登录！");
                return;
            }
            MessageEditActivity.openActivity(this);
        });
        list=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adpProList=new MsgAdapter(this,list);
        binding.recyclerMsg.setAdapter(adpProList);
        binding.recyclerMsg.setLayoutManager(linearLayoutManager);
        adpProList.setOnItemClickListener(this);
    }
    private void setupViewAction(){
        viewModel.getMsgAllModel().observe(this,item->{
            list.clear();
            if(item==null||item.size()==0){
                binding.recyclerMsg.setVisibility(View.INVISIBLE);
                binding.layEmpty.setVisibility(View.VISIBLE);
            }else{
                binding.recyclerMsg.setVisibility(View.VISIBLE);
                binding.layEmpty.setVisibility(View.INVISIBLE);
                list.clear();
                list.addAll(item);
                adpProList.setList(list);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getMsgAll();
    }

    @Override
    public void onVillProjectClickListener(MsgBean item) {

    }
}