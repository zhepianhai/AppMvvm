package com.gw.zph.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.gw.zph.R;
import com.gw.zph.core.ConstantKt;
import com.gw.safty.common.utils.file.FilePath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import com.gw.zph.databinding.DownloadDialogBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

@SuppressWarnings("deprecation")
public class DownloadDialog extends Dialog {
    private String downloadPath = "";
    public static String fileSavePath = FilePath.INSTANCE.getFileCachePath();
    private DownloadDialogBinding binding;

    public DownloadDialog(@NonNull Context context) {
        super(context);
    }

    public DownloadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DownloadDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Context context = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_download, null, false);
        setContentView(binding.getRoot());
        binding.btnRestar.setOnClickListener(btn -> {
            startDownload(downloadPath);
        });
        Window window = getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            if (context instanceof Activity) {
                WindowManager wm = ((Activity) context).getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.CENTER);
                lp.width = (int) (width * 0.8);
                window.setAttributes(lp);
            }
        }
    }

    public void startDownload(String url) {
        downloadPath = url;
        this.show(); //必须先调用show,才会初始化界面，否则dataBinding会空
        DownloadHandler downloadHandler = new DownloadHandler(Looper.myLooper(), this);
        new Thread(new DownloadThread(downloadHandler, url)).start();
    }

    @SuppressLint("SetTextI18n")
    private void onDownloadStarting(int totalSize) {
        binding.pbDownloadProgress.setVisibility(View.VISIBLE);
        binding.tvDownloadProgress.setVisibility(View.VISIBLE);
        binding.btnRestar.setVisibility(View.INVISIBLE);
        binding.pbDownloadProgress.setMax(totalSize);
        binding.tvDownloadProgress.setText("0B/" + formatFileSize(totalSize));
    }

    @SuppressLint("SetTextI18n")
    private void onProgressChange(int current, int total) {
        binding.pbDownloadProgress.setProgress(current);
        String c = formatFileSize(current);
        String t = formatFileSize(total);
        Timber.d("download progress %s/%s", c , t);
        binding.tvDownloadProgress.setText(c + "/" + t);
    }

    // 下载完成
    private void onFinish(String path) {
        dismiss();
        installApk(path);
    }

    private void onError() {
        binding.tvDownloadProgress.setVisibility(View.INVISIBLE);
        binding.pbDownloadProgress.setVisibility(View.INVISIBLE);
        binding.btnRestar.setVisibility(View.VISIBLE);
    }

    private void installApk(String path) {
        Context context = getContext();
        File file = new File(path);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);
    }

    private static class DownloadHandler extends Handler {
        private WeakReference<DownloadDialog> dialogRef;

        public DownloadHandler(@NonNull Looper looper, DownloadDialog dialogRef) {
            super(looper);
            this.dialogRef = new WeakReference<>(dialogRef);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            DownloadDialog dialog = dialogRef.get();
            if (dialog == null) return;
            switch (msg.what) {
                case 1:
                    dialog.onDownloadStarting(msg.arg1);
                    break;
                case 2:
                    dialog.onProgressChange(msg.arg2, msg.arg1);
                    break;
                case 3:
                    dialog.onError();
                    break;
                case 4:
                    dialog.onFinish(msg.obj.toString());
                    break;
            }
        }
    }

    private static class DownloadThread implements Runnable {
        DownloadHandler handler;
        String url;

        public DownloadThread(DownloadHandler downloadHandler, String url) {
            this.handler = downloadHandler;
            this.url = url;
        }

        @Override
        public void run() {
            File dirPath = new File(DownloadDialog.fileSavePath, ConstantKt.APP_UPDATE_APK);//"updateApk/"
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
            File file = new File(dirPath, "updata.apk");
            Request request = new Request.Builder().url(url).build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // 下载失败
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Message message = handler.obtainMessage();
                    long total = response.body().contentLength();
                    message.what = 1;
                    message.arg1 = (int) response.body().contentLength();
                    handler.sendMessage(message);
                    InputStream inputStream = response.body().byteStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    try {
                        byte[] buffer = new byte[2048];
                        int len;
                        int sum = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                            sum += len;
                            Message message1 = handler.obtainMessage();
                            message1.what = 2;
                            message1.arg2 = sum;
                            message1.arg1 = (int) total;
                            handler.sendMessage(message1);
                        }
                        Message finishMsg = handler.obtainMessage(4);
                        finishMsg.obj = file.getAbsolutePath();
                        handler.sendMessage(finishMsg);
                    } catch (Exception e) {
                        Message message1 = handler.obtainMessage();
                        message1.what = 3;
                        message1.obj = e.getMessage();
                        handler.sendMessage(message1);
                    } finally {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }

                }
            });
        }
    }

    private static String formatFileSize(long size) {
        String errorResult = "0B";
        if (size == 0) {
            return errorResult;
        }
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            String formatSize;
            if (size < 1024) {
                formatSize = df.format((double) size) + "B";
            } else if (size < 1048576) {
                formatSize = df.format((double) size / 1024) + "KB";
            } else if (size < 1073741824) {
                formatSize = df.format((double) size / 1048576) + "MB";
            } else {
                formatSize = df.format((double) size / 1073741824) + "GB";
            }
            return formatSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorResult;
    }
}
