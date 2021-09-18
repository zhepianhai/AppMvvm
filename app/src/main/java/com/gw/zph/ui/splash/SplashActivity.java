package com.gw.zph.ui.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.gw.safty.common.extensions.ActivityExtensionsKt;
import com.gw.safty.common.extensions.NumbericalExtKt;
import com.gw.safty.common.network.SecureNetworktConfig;
import com.gw.zph.BuildConfig;
import com.gw.zph.R;
import com.gw.zph.base.BaseActivityImpl;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.model.splash.bean.AppVersionInfoBean;
import com.gw.zph.ui.dialog.DownloadDialog;
import com.gw.zph.ui.dialog.UpdateDialog;
import com.gw.zph.ui.home.MainActivity;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

@SuppressWarnings("ConstantConditions")
public class SplashActivity extends BaseActivityImpl {
    private static final int REQUIRE_PERMISSION = 1;
    private static final int REQ_LOGIN = 0x1002;

    private SplashActViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = createViewModel(SplashActViewModel.class);

        setupGlobalHttps();
        //根据判断显示不同的界面
        setContentView(R.layout.activity_splash);

        start();
    }

    private void setupGlobalHttps() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        InputStream keyInputstream = null;
        try {
            keyInputstream = getAssets().open("zph.bks");
            SecureNetworktConfig.SSLParams sslSocketFactory = SecureNetworktConfig.getSslSocketFactory(null, keyInputstream, "123456");
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory.sSLSocketFactory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查完所有限制内容项并申请权限之后，调此方法进行业务相关的内容
     **/
    private void start() {
        setupViewModel();
        getIntentExtra();
        new Handler().postDelayed(() -> {
            defaultLoadMain();
        }, 3000);
//        viewModel.loadVersionInfo(String.valueOf(BuildConfig.VERSION_CODE));
    }

    private void setupViewModel() {
        setupBaseViewModel(viewModel);
//        viewModel.getVersionInfo().observe(this, version -> {
//            if (null == version) {
//                defaultLoadMain();
//            } else {
//                showUpdateDialog(version);
//            }
//        });
    }

    private void getIntentExtra() {
    }

    @Override
    protected void reLogin() {
    }

    private UpdateDialog mUpdateDialog;

    private void showUpdateDialog(AppVersionInfoBean appVersionInfoBean) {
        mUpdateDialog = new UpdateDialog(this, appVersionInfoBean);
        int versionCode = BuildConfig.VERSION_CODE;

        String appVersionCodeStr = appVersionInfoBean.getVersion();
        double appVersionCode = 0.0;
        if (!TextUtils.isEmpty(appVersionCodeStr)) {
            appVersionCode = NumbericalExtKt.asDouble(appVersionCodeStr, 1);
        }
        if (versionCode < appVersionCode && null != mUpdateDialog && !mUpdateDialog.isShowing()) {
            mUpdateDialog.show();
            mUpdateDialog.setUpdateInterface(confirm -> {
                if (confirm) {
                    downLoadTag = 1;
                    if (hasExternalStoragePermission()) {
                        DownloadDialog downloadDialog = new DownloadDialog(SplashActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
                        downloadDialog.startDownload(BuildConfig.BASE_URL + "pdcApi/version/download");
                    } else {
                        getFilePermission();
                    }
                } else {
                    defaultLoadMain();
                }
            });
        } else {
            defaultLoadMain();
        }
    }

    /**
     * 下载标识项
     */
    private int downLoadTag = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (permissions.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (downLoadTag == 1) {
                            DownloadDialog downloadDialog = new DownloadDialog(SplashActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
                            downloadDialog.startDownload(BuildConfig.BASE_URL + "bis/version/download");
                        }
                    } else {
                        ActivityExtensionsKt.exShortToast(SplashActivity.this, "拒绝相关权限将无法更新软件");
                    }
                }
                break;
        }

    }

    private boolean hasExternalStoragePermission() {
        int perm = checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private void getFilePermission() {
        ArrayList<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
            }

            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUIRE_PERMISSION);
            }
        }
    }

    /**
     * 默认进入主页面,或者跳转登录界面获取授权信息
     */
    private void defaultLoadMain() {
//        if (StatusHolder.INSTANCE.getUserBean() == null) {
            Intent intent = new Intent(this,  MainActivity.class);
            startActivityForResult(intent, REQ_LOGIN);
            finish();
//        } else {
//            finish();
//        }
    }

    @Override
    public void finish() {
        super.finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            overridePendingTransition(R.anim.anim_alpha_no_change, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOGIN) {
            if (resultCode == RESULT_OK) {
                defaultLoadMain();
            } else {
                finish();
            }
        }
    }
}
