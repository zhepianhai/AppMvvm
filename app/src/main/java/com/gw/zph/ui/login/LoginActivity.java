package com.gw.zph.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.databinding.DataBindingUtil;
import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.databinding.LoginActivityBinding;

public class LoginActivity extends BaseActivityImpl {
    private LoginViewModel viewModel;
 //    private CaptureImageBean savedImageData;//图片验证码中返回的信息
    private LoginActivityBinding binding;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_new);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_new );
        viewModel = createViewModel(LoginViewModel.class);
        setupBaseViewModel(viewModel);
        initObserver();
        setupViewAction();
    }
    private void setupViewAction() {
        binding.llBack.setOnClickListener(v->{
            this.finish();
        });
        binding.btnLogin.setOnClickListener(v->{

        });
        binding.btnLoginOth.setOnClickListener(v->{

        });
    }

    private void initObserver() {
        setupBaseViewModel(viewModel);

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