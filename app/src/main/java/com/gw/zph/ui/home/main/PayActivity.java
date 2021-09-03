package com.gw.zph.ui.home.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.databinding.DataBindingUtil;

import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.databinding.PayActivityBinding;
import com.gw.zph.view.PayDialog;
import com.gw.zph.view.PosSucDialog;

import static android.view.KeyEvent.KEYCODE_BACK;


/**
 * 支付
 */
public class PayActivity extends BaseActivityImpl implements PayDialog.OnClickBottomListener {
    private PayActivityBinding binding;
    private int flag=0;
    private PayDialog dialog;
    public static void openActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay);
        binding.setLifecycleOwner(this);
        binding.getRoot().setFitsSystemWindows(true);
        init();
    }
    private void init(){
        binding.llBack.setOnClickListener(v -> {
            back();
        });
        binding.btnPay.setOnClickListener(v->pay());
        binding.tv0.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        binding.tv1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        binding.tv2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        binding.lay1.setOnClickListener(v->{
            flag=0;
            showBg(true,false,false);
        });
        binding.lay2.setOnClickListener(v->{
            flag=1;
            showBg(false,true,false);
        });
        binding.lay3.setOnClickListener(v->{
            flag=2;
            showBg(false,false,true);
        });
    }
    private void showBg(boolean falg1,boolean falg2,boolean falg3){
        binding.lay1.setBackground(!falg1 ? getDrawable(R.drawable.edit_bg) : getDrawable(R.drawable.lay_zf_select));
        binding.lay2.setBackground(falg2?getDrawable(R.drawable.lay_zf_select):getDrawable(R.drawable.edit_bg));
        binding.lay3.setBackground(falg3?getDrawable(R.drawable.lay_zf_select):getDrawable(R.drawable.edit_bg));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK) {
            back();
            return false;
        }
        return false;
    }
    private void back(){
        if(dialog==null){
            dialog=new PayDialog(this);
        }
        dialog.setOnClickBottomListener(this);
        dialog.show();

    }

    @Override
    public void onPositiveClick(int type) {
        if(type==0){
            //离开
            this.finish();
        }else{
            //支付
            pay();
        }
    }
    private void  pay(){

    }
}