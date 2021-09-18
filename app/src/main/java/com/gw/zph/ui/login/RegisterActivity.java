package com.gw.zph.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.gw.safty.common.utils.ToastUtil;
import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.databinding.LoginActivityBinding;
import com.gw.zph.databinding.RegisterActivityBinding;
import com.gw.zph.model.login.bean.UserBean;
import com.gw.zph.modle.AllVm;
import com.gw.zph.utils.Constants;

public class RegisterActivity extends BaseActivityImpl {
    private RegisterActivityBinding binding;
    private AllVm viewModel;
    public static void openActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register );
        viewModel = createViewModel(AllVm.class);
        binding.getRoot().setFitsSystemWindows(true);
        setupBaseViewModel(viewModel);
        initObserver();
        setupViewAction();
    }
    private void initObserver() {
        setupBaseViewModel(viewModel);
        viewModel.getLoginModel().observe(this,bean->{

        });
        viewModel.getRegisterModel().observe(this, this::onChanged);
    }
    private void setupViewAction(){
        binding.llBack.setOnClickListener(v->{
            this.finish();
        });
        binding.btnLoginPsd.setOnClickListener(v->{
            //账号密码登录
            register();
        });
    }
    private void register(){
        String phone=binding.etPhone.getText().toString().trim();
        String psd1=binding.etPsd.getText().toString().trim();
        String psd2=binding.etPsd1.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToast(this,"请输入手机号！");
            return;
        }
        if(TextUtils.isEmpty(psd1)){
            ToastUtil.showToast(this,"请输入密码！");
            return;
        }
        if(TextUtils.isEmpty(psd2)){
            ToastUtil.showToast(this,"请再次输入密码！");
            return;
        }
        if(!psd2.equals(psd1)){
            ToastUtil.showToast(this,"两次密码不一致！");
            return;
        }

        UserBean userBean=new UserBean();
        userBean.setUsername(phone);
        userBean.setPhone(phone);
        userBean.setPersid(phone);
        userBean.setPsd(psd1);
        userBean.setStatus("1");
        viewModel.register(userBean);
    }

    private void onChanged(UserBean bean) {
        if (bean != null) {
            ToastUtil.showToast(this, "注册成功！");
            RegisterActivity.this.finish();
        } else {
            ToastUtil.showToast(this, "注册失败！");
        }
    }
}