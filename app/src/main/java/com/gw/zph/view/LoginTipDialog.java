package com.gw.zph.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.gw.zph.R;
import com.gw.zph.ui.login.LoginActivity;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.Objects;

public class LoginTipDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private QMUIRoundButton btnOk,btnDis;
    public LoginTipDialog(@NonNull Context context) {
        super(context);
        this.mContext=context;
    }

    public LoginTipDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_tip);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        btnOk=findViewById(R.id.btn_ok);
        btnDis=findViewById(R.id.btn_dis);
        btnDis.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dis:
                dismiss();
                break;
            case R.id.btn_ok:
                LoginActivity.openActivity(mContext);
                dismiss();
                break;
        }
    }

}
