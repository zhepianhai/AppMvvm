package com.gw.zph.ui.commend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.core.ConstantKt;
import com.gw.zph.databinding.LoginActivityBinding;
import com.gw.zph.databinding.PrivatePolicyActivityBinding;
import com.gw.zph.ui.login.LoginActivity;

public class PrivatePolicyActivity extends BaseActivityImpl {
    private PrivatePolicyActivityBinding binding;
    private int type;
    public static int TYPE_1=1;
    public static int TYPE_2=2;
    public static void openActivity(Context context,int type) {
        Intent intent = new Intent();
        intent.setClass(context, PrivatePolicyActivity.class);
        intent.putExtra(ConstantKt.BUNDLE_NAME,type);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_policy);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_private_policy );
        binding.getRoot().setFitsSystemWindows(true);
        initData();
    }

    private void initData() {
        if(getIntent()!=null){
            type=getIntent().getIntExtra(ConstantKt.BUNDLE_NAME,0);
        }
        binding.llBack.setOnClickListener(v->{
            this.finish();
        });
        if(type==TYPE_1){
            binding.tvTitle.setText("隐私协议");
            binding.tvContent.setText(Html.fromHtml(getResources().getString(R.string.privacy_policy)));
        }else if(type==TYPE_2){
            binding.tvTitle.setText("用户协议");
            binding.tvContent.setText(Html.fromHtml(getResources().getString(R.string.user_agreement)));
        }
    }
}