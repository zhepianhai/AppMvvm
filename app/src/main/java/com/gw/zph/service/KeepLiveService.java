package com.gw.zph.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
/**
 * 守护进程服务
 * */
public class KeepLiveService extends Service {
    MyConn conn;
    MyBinder binder;
    public KeepLiveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return  binder;
    }

    class MyBinder extends Binder {
        public String getServiceName()  {
            return KeepLiveService.class.getSimpleName();
        }
    }

    class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Intent intent = new Intent(KeepLiveService.this, LocationService.class);
            //开启本地服务
            if (Build.VERSION.SDK_INT >= 26) {
               //适配8.0机制
                KeepLiveService.this.startForegroundService(intent);
            } else {
                KeepLiveService.this.startService(intent);
            }
            //绑定本地服务
            KeepLiveService.this.bindService(new Intent(KeepLiveService.this, LocationService.class), conn, Context.BIND_IMPORTANT);
        }

    }
    @Override
    public void onCreate() {
        super.onCreate();
        conn = new MyConn();
        binder = new MyBinder();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, LocationService.class), conn, Context.BIND_IMPORTANT);
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        Intent intent = new Intent(KeepLiveService.this, LocationService.class);
        //开启本地服务
        if (Build.VERSION.SDK_INT >= 26) {
           //适配8.0机制
            KeepLiveService.this.startForegroundService(intent);
        } else {
            KeepLiveService.this.startService(intent);
        }
        //绑定本地服务
        KeepLiveService.this.bindService(new Intent(KeepLiveService.this, LocationService.class), conn, Context.BIND_IMPORTANT);
        Intent intent1 = new Intent("com.gw.zph.KeepLiveService");
        sendBroadcast(intent1);
        super.onDestroy();
    }
}
