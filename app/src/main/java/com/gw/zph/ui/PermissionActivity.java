package com.gw.zph.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gw.safty.common.extensions.ActivityExtensionsKt;
import com.gw.safty.common.extensions.CommonExtensionsKt;
import com.gw.zph.R;
import com.gw.zph.core.Device;
import com.gw.zph.ui.splash.SplashActivity;
import com.gw.zph.view.PermissionDialog;

import timber.log.Timber;

/**
 * 只是处理权限请求的Activity
 */
public class PermissionActivity extends AppCompatActivity implements PermissionDialog.OnClickBottomListener {
    private boolean requestSettingPermissions = false; //在app跳转到设置界面时做标志，未授权直接返回造成跳过权限检测
    private static final int REQUIRE_ALL_PERMISSION = 0x1001;
    private PermissionDialog permissionDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Device.INSTANCE.setup(this);
        boolean isSimulator = Device.INSTANCE.isSimulator();
        boolean isRoot = Device.INSTANCE.isRoot();
//        if (isSimulator || isRoot) { //判断是否模拟器或者root设备， 用同一个dialog，显示不同的message
//            String msg = isSimulator ? getString(R.string.app_name_safe_content) : getString(R.string.app_name_safe_content_root);
//            showSafetyWarning(msg);
//            return;
//        }
        //请求权限相关
        if (CommonExtensionsKt.isOverM()) {
            if (hasPermissions(this)) {
                start();
            } else {
                showPermissionUI();

            }
        } else {
            start();
        }
    }
    private void showPermissionUI(){
        permissionDialog=new PermissionDialog(this);
        permissionDialog.setOnClickBottomListener(this);
        permissionDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestSettingPermissions) { //如果跳转到设置，再回来
            requestSettingPermissions = false;
            if (hasPermissions(this)) { //重新检查是否再设置界面授予了权限
                start();
            } else {
                showPermissionWarning();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUIRE_ALL_PERMISSION) {
            if (allPermissionGranted(grantResults)) {
                start();
            } else {
                showPermissionWarning();
            }
        }
    }

    private void start() {
        ActivityExtensionsKt.exOpenActivity(this, SplashActivity.class, null, true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_alpha_no_change, 0);
    }

    private void showPermissionWarning() {
        new AlertDialog
                .Builder(this)
                .setTitle("警示")
                .setMessage("权限被拒绝，将有可能发生错误，请到设置界面授予权限")
                .setCancelable(false)
                .setPositiveButton("去授予权限", (dialog, which) -> {
                    dialog.dismiss();
                    requestSettingPermissions = true;
                    Uri uri = Uri.fromParts("package", getPackageName(), "PermissionsFragment");
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("退出应用", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).show();
    }

    private void showSafetyWarning(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("" + getString(R.string.app_name_safe_title));
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("立即退出", (dialog, w) -> {
            finish();
            dialog.dismiss();
        });
        builder.create().show();
    }

    @SuppressLint("InlinedApi")
    private static String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.FOREGROUND_SERVICE
    };

    public static boolean hasPermissions(Context context) {
        for (String permission : permissions) {
            int i = ContextCompat.checkSelfPermission(context, permission);
            boolean isGranted = i == PackageManager.PERMISSION_GRANTED;
            Timber.d(permission + ":" + i);
            if (!isGranted) return false;
        }
        return true;
    }

    public static boolean allPermissionGranted(int[] granted) {
        for (int grantResult : granted) {
            boolean result = grantResult == PackageManager.PERMISSION_GRANTED;
            if (!result) return false;
        }
        return true;
    }
    //提示框回调
    @Override
    public void onPositiveClick() {
        if (CommonExtensionsKt.isOverM()) {
            requestPermissions(permissions, REQUIRE_ALL_PERMISSION);
        }
    }
}
