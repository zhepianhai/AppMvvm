package com.gw.zph.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.gw.zph.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.Objects;

public class PayDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private QMUIRoundButton btnOk,btnDis;
    public PayDialog(@NonNull Context context) {
        super(context);
    }

    public PayDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pay_dialog);
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
                onClickBottomListener.onPositiveClick(0);
                dismiss();
                break;
            case R.id.btn_ok:
                onClickBottomListener.onPositiveClick(1);
                dismiss();
                break;
        }
    }
    public OnClickBottomListener onClickBottomListener;
    public void setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
    }
    public interface OnClickBottomListener{
        void onPositiveClick(int type);
    }
}
