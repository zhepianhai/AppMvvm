package com.gw.zph.ui.message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.databinding.MessageListActivityBinding;
import com.gw.zph.databinding.MyLetterActivityBinding;
import com.gw.zph.modle.bean.MsgBean;
import com.gw.zph.ui.home.list.MyLetterActivity;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends BaseActivityImpl {
    private MessageListActivityBinding binding;
    private List<MsgBean> list;
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
        initObserver();
        setupViewAction();
    }
    private void initObserver(){
        binding.llBack.setOnClickListener(v->{
            this.finish();
        });
        list=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    }
    private void setupViewAction(){}
}