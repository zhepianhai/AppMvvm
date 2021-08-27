package com.gw.zph.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.gw.zph.R;
import com.gw.zph.databinding.UpdateDialogBinding;
import com.gw.zph.model.splash.bean.AppVersionInfoBean;

import java.util.Objects;

public class UpdateDialog extends AlertDialog {
    private AppVersionInfoBean versionInfoBean;

    private Context mContext;
    private UpdateDialogBinding binding;

    public UpdateDialog(@NonNull Context context, AppVersionInfoBean versionInfoBean) {
        super(context, R.style.update_dialogStyle);
        this.mContext = context;
        this.versionInfoBean = versionInfoBean;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_update_layout, null, false);
        setContentView(binding.getRoot());

        this.setCancelable(false);
        Window dialogWindow = Objects.requireNonNull(this.getWindow());
        //隐藏键盘
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        WindowManager wm = ((Activity) mContext).getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = (int) (width * 0.8);
        dialogWindow.setAttributes(lp);

        initData();

    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        /*1为强制更新 0为不强制更新 **/
        int forceUpdate =0;
        String updateContent="";

        if (null!=versionInfoBean){
            forceUpdate = versionInfoBean.getForceUpdate();
            updateContent=versionInfoBean.getUpdateContent();
        }
        if (forceUpdate == 1) {
            binding.ivCancelDownload.setVisibility(View.GONE);
            binding.tvForceTips.setText("强制更新");
        } else {
            binding.tvForceTips.setText("非强制更新");
            binding.ivCancelDownload.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(updateContent)){
            updateContent="马上更新";
        }
        binding.tvReleaseNote.setText(updateContent);

        String pkName =mContext.getPackageName();
        String newVersionName=versionInfoBean.getAppName();
        String versionName;
        try {
            versionName = mContext.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName="v0.0";
        }
        binding.tvCurrentVersion.setText("当前版本："+versionName);

        binding.tvNewVersion.setText("最新版本："+newVersionName);

        binding.ivCancelDownload.setOnClickListener(v -> {
            dismiss();
            if (null!=mUpdateInterface){
                mUpdateInterface.call(false);
            }
        });
        binding.tvUpdate.setOnClickListener(v -> {
            dismiss();
            if (null!=mUpdateInterface){
                mUpdateInterface.call(true);
            }
        });

    }

    private DoubleChoiceCallback mUpdateInterface;

    public void setUpdateInterface(DoubleChoiceCallback mUpdateInterface) {
        this.mUpdateInterface = mUpdateInterface;
    }
}
