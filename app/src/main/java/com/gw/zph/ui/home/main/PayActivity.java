package com.gw.zph.ui.home.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.databinding.PayActivityBinding;


/**
 * 支付
 */
public class PayActivity extends BaseActivityImpl {
    private PayActivityBinding binding;

    public static void openActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay);
    }
}