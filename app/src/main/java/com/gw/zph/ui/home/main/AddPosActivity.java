package com.gw.zph.ui.home.main;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.databinding.AddPosBinding;
import com.gw.zph.databinding.LoginActivityBinding;
import com.gw.zph.utils.MCString;
import com.gw.zph.view.PermissionDialog;
import com.gw.zph.view.PosSucDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import static android.view.KeyEvent.KEYCODE_BACK;

public class AddPosActivity extends BaseActivityImpl implements PosSucDialog.OnClickBottomListener {
    private AddPosBinding binding;
    private PosSucDialog posSucDialog;
    public static void openActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AddPosActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_pos);
        binding.setLifecycleOwner(this);
        binding.getRoot().setFitsSystemWindows(true);
        initListener();
    }

    private void initListener() {
        binding.setLifecycleOwner(this);
        binding.llBack.setOnClickListener(v -> {
            back();
        });
        binding.btnLocation.setOnClickListener(v -> addLocation());
    }

    private void addLocation() {
        String num = binding.etPhone.getText().toString().trim();
        if (!MCString.isMobileNO(num)) {
            QMUITipDialog dialog = new QMUITipDialog.Builder(this).
                    setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord("请输入有效的手机号码").create();
            dialog.show();
            new Handler().postDelayed(() -> {
                dialog.dismiss();
            }, 3000);
            return;
        }
        QMUITipDialog dialog = new QMUITipDialog.Builder(this).
                setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("定位中...").create();
        dialog.show();
        new Handler().postDelayed(() -> {
            dialog.dismiss();
            showLocationSuccess();
        }, 5000);


    }

    private void showLocationSuccess() {
        if(posSucDialog==null){
            posSucDialog=new PosSucDialog(this);
        }
        posSucDialog.setOnClickBottomListener(this);
        posSucDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK) {
            back();
            return false;
        }
        return false;
    }

    private void back() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("提示" );
        builder.setMessage("确定要退出添加吗？");

        builder.setPositiveButton("确定", (arg0, arg1) -> {
            // TODO Auto-generated method stub
            finish();
            arg0.dismiss();
        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }


    @Override
    public void onPositiveClick() {
        //点击解锁
        PayActivity.openActivity(this);
    }
}