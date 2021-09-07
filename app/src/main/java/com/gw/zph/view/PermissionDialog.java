package com.gw.zph.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gw.zph.R;
import com.gw.zph.ui.commend.PrivatePolicyActivity;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.Objects;

public class PermissionDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private QMUIRoundButton btnDis,btnOk;
    public PermissionDialog(@NonNull Context context) {
        this(context,0);
        mContext=context;
    }
    public PermissionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
    }

    protected PermissionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_permission_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        btnDis=findViewById(R.id.btn_dis);
        btnOk=findViewById(R.id.btn_ok);
        btnDis.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        findViewById(R.id.tvUser).setOnClickListener(this);
        findViewById(R.id.tvPri).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvUser:
                PrivatePolicyActivity.openActivity(mContext,PrivatePolicyActivity.TYPE_2);
                break;
            case R.id.tvPri:
                PrivatePolicyActivity.openActivity(mContext,PrivatePolicyActivity.TYPE_1);
                break;
            case R.id.btn_dis:
                System.exit(0);
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
