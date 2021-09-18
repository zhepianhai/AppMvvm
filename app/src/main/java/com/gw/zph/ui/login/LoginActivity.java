package com.gw.zph.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.cmic.sso.sdk.auth.AuthnHelper;
import com.cmic.sso.sdk.auth.TokenListener;
import com.gw.safty.common.utils.ToastUtil;
import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.core.ConstantKt;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.databinding.LoginActivityBinding;
import com.gw.zph.model.login.bean.UserBean;
import com.gw.zph.modle.AllVm;
import com.gw.zph.ui.commend.PrivatePolicyActivity;
import com.gw.zph.ui.home.main.AddPosActivity;
import com.gw.zph.utils.Constants;

import org.json.JSONObject;

public class LoginActivity extends BaseActivityImpl {
    private AllVm viewModel;
 //    private CaptureImageBean savedImageData;//图片验证码中返回的信息
    private LoginActivityBinding binding;
    private CountDownTimer timer;
    private AuthnHelper authnHelper;
    public static void openActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_new);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_new );
        viewModel = createViewModel(AllVm.class);
        binding.getRoot().setFitsSystemWindows(true);
        authnHelper=AuthnHelper.getInstance(this);
        setupBaseViewModel(viewModel);
        initObserver();
        setupViewAction();
    }
    private void setupViewAction() {
        binding.llBack.setOnClickListener(v->{
            this.finish();
        });
        binding.btnLogin.setOnClickListener(v->{
            //一键登录
        });
        binding.btnLoginOth.setOnClickListener(v->{
            //其它方式登录
            binding.centerOneView.setVisibility(View.INVISIBLE);
            binding.centerTwoView.setVisibility(View.VISIBLE);

        });
        binding.btnLoginPsd.setOnClickListener(v->{
            //账号密码登录
            commit();
        });
        binding.tvOne.setOnClickListener(v->{
            //切换一键
            binding.centerOneView.setVisibility(View.VISIBLE);
            binding.centerTwoView.setVisibility(View.INVISIBLE);
        });
        binding.tvTwo.setOnClickListener(v->{
            //注册
            RegisterActivity.openActivity(this);
        });
        binding.tvUser.setOnClickListener(v->{
            PrivatePolicyActivity.openActivity(this,PrivatePolicyActivity.TYPE_2);
        });
        binding.tvPri.setOnClickListener(v->{
            PrivatePolicyActivity.openActivity(this,PrivatePolicyActivity.TYPE_1);
        });
    }

    private void initObserver() {
        setupBaseViewModel(viewModel);
        viewModel.getLoginModel().observe(this,bean->{
            if(bean!=null){
                ToastUtil.showToast(this,"登录成功");
                StatusHolder.INSTANCE.setLoginUserBean(bean,this);
                LoginActivity.this.finish();
            }else{
                ToastUtil.showToast(this,"登录失败");
            }
        });
        viewModel.getRegisterModel().observe(this,bean->{

        });
        authnHelper.getPhoneInfo(Constants.CHINA_MOB_APPID, Constants.CHINA_MOB_APPKEY, (i, jsonObject) ->
                Log.i("TAGG","jsonObject"+jsonObject.toString()));
    }
    private void commit(){
        if(!binding.checkbox.isChecked()){
            ToastUtil.showToast(this,"请同意用户协议和隐私协议");
            return;
        }
        String phone=binding.etPhone.getText().toString().trim();
        String psd=binding.etPsd.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToast(this,"请输入手机号！");
            return;
        }
        if(TextUtils.isEmpty(psd)){
            ToastUtil.showToast(this,"请输入密码！");
            return;
        }
        UserBean userBean=new UserBean();
        userBean.setPhone(phone);
        userBean.setPsd(psd);
        viewModel.login(userBean);
    }

    @Override
    protected void reLogin() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_alpha_no_change, 0);
    }

    public void login() {

    }
}