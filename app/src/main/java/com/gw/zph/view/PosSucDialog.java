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

public class PosSucDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private ImageView close;
    private QMUIRoundButton btnOk;
    public PosSucDialog(@NonNull Context context) {
        this(context,0);
        mContext=context;
    }

    public PosSucDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_possuc_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        close=findViewById(R.id.img_close);
        btnOk=findViewById(R.id.btn_ok);
        close.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close:
                dismiss();
                break;
            case R.id.btn_ok:
                onClickBottomListener.onPositiveClick();
                dismiss();
                break;
        }
    }

    public OnClickBottomListener onClickBottomListener;
    public void setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
    }
    public interface OnClickBottomListener{
        void onPositiveClick();
    }
}
