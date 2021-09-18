package com.gw.zph.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.gw.safty.common.network.BaseResponse;
import com.gw.safty.common.network.CommonResponse;
import com.gw.safty.common.utils.JSDateUtil;
import com.gw.safty.common.utils.NetUtil;
import com.gw.zph.application.MyApplication;
import com.gw.zph.base.db.DbHelper;
import com.gw.zph.base.db.dao.OffLineLatLngInfo;
import com.gw.zph.base.db.dao.OffLineLatLngInfoDao;
import com.gw.zph.core.StatusHolder;
import com.gw.zph.core.network.RetrofitClient;
import com.gw.zph.model.login.bean.UserBean;
import com.gw.zph.modle.MapService;
import com.gw.zph.modle.MapService1;
import com.gw.zph.utils.FileUtil;
import com.gw.zph.utils.MapUtils;
import com.gw.zph.utils.ThreadPoolUtil;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author zhepianhai
 * <p>
 * 位置上传服务
 * 1.有网情况走高精度定位，无网情况走GPS定位
 * 2，存数据库逻辑：上一条记录时间超过1分钟直接存储，一分钟以内情况下距离超过5米直接存储。
 * 3. 上传逻辑：有网情况大于100条记录，直接上传。小于100条记录情况判断最老的一条数据，间隔超过5分钟，全部上传
 * 4. 分批次上传，上传数量大于100条：以100为单位进行分割，分批次上传。
 */
public class LocationService extends Service {

    public final String TAG = "LocationService";
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private Timer mTimer;
    private LatLng last_latlng;
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date mCurrentMapLocation;
    private static final int SIZE = 20;
    private static final int ONEMINIUE = 60 * 1_000;

    MyBinder binder;
    MyConn conn;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    class MyBinder extends Binder {

        public String getServiceName() {
            return LocationService.class.getSimpleName();
        }
    }

    class MyConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Intent intent = new Intent(LocationService.this, KeepLiveService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                //适配8.0机制
                LocationService.this.startForegroundService(intent);
            } else {
                LocationService.this.startService(intent);
            }
            //绑定远程服务
            LocationService.this.bindService(new Intent(LocationService.this, KeepLiveService.class), conn, Context.BIND_IMPORTANT);
        }
    }

    @Override
    public void onCreate() {
        binder = new MyBinder();
        conn = new MyConn();
//        Notification noti = new Notification();
//        noti.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
//        startForeground(1, noti);
//        initLocation();
//        try {
//            MyNotificationManagerUtil myNotificationManagerUtil = new MyNotificationManagerUtil(this);
//            myNotificationManagerUtil.setOngoing(true);
//            myNotificationManagerUtil.setPriority(Notification.PRIORITY_HIGH);
//            myNotificationManagerUtil.setOnlyAlertOnce(true);
//            myNotificationManagerUtil.sendNotification(1, "超级定位", "超级定位后台运行中...", R.mipmap.ic_launcher);
//        } catch (Exception e) {
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        initSinglePixe();
//        mTimer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
                initLocation();
//                locationClient.startLocation();
//            }
//        };
//        mTimer.scheduleAtFixedRate(task, 0, 30 * 1_000);

        this.bindService(new Intent(LocationService.this, KeepLiveService.class), conn, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

//        if (locationClient != null) {
//            locationClient.stopLocation();
//            destroyLocation();
//        }
//        if (null != mTimer) {
//            mTimer.cancel();
//        }
//        try {
//            MyNotificationManagerUtil myNotificationManagerUtil = new MyNotificationManagerUtil(this);
//            myNotificationManagerUtil.clearNotification();
//        } catch (Exception e) {
//        }
        Intent intent = new Intent(LocationService.this, KeepLiveService.class);
        //适配8.0机制
        if (Build.VERSION.SDK_INT >= 26) {
            LocationService.this.startForegroundService(intent);
        } else {
            LocationService.this.startService(intent);
        }
        LocationService.this.bindService(new Intent(LocationService.this, KeepLiveService.class), conn, Context.BIND_IMPORTANT);
        Intent intent1 = new Intent("com.gw.zph.service.LocationService");
        sendBroadcast(intent1);
        super.onDestroy();
    }

    private void initLocation() {
        if (locationClient == null) {
            //初始化client
            locationClient = new AMapLocationClient(this.getApplicationContext());
        }
        //初始化定位参数
        initLocationOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        if (locationListener == null) {
            locationListener = loc -> {
                if (null != loc) {//定位成功
                    UpDate(loc);
                } else {//定位失败
                }
//                locationClient.stopLocation();
//                destroyLocation();
            };
        }
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

    private void initLocationOption() {

        if (null == locationOption) {
            locationOption = new AMapLocationClientOption();
        }
        if (NetUtil.hasNetwork(getApplicationContext())) {
            //定位精度:高精度模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        } else {
            //定位精度:GPS模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        }
        //设置定位缓存策略
        locationOption.setLocationCacheEnable(false);
        //gps定位优先
        locationOption.setGpsFirst(false);
        //设置定位间隔
        locationOption.setInterval(60000);
        locationOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        locationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
//        locationOption.setOnceLocationLatest(true);//true表示获取最近3s内精度最高的一次定位结果；false表示使用默认的连续定位策略。
        locationOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        //AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = loc -> {
        if (null != loc) {//定位成功
            UpDate(loc);
        }
//        locationClient.stopLocation();
//        destroyLocation();
    };

    /**
     * 上传更新
     */
    private void UpDate(AMapLocation loc) {
        try {
            if (mCurrentMapLocation == null) {
                mCurrentMapLocation = new Date();
            }
            final LatLng newlatlng = new LatLng(loc.getLatitude(), loc.getLongitude());
            Log.i("TAGG", "ZG位置：" + JSDateUtil.getDataStringByObj(loc.getAddress()));

            MyApplication myApplication = (MyApplication) this.getApplication();
            if (myApplication == null) {
                return;
            }
            if (StatusHolder.INSTANCE.getCurUserBean(getApplicationContext()) == null) {
                return;
            }
            if (null != loc && myApplication.getMamapl() != null) {
                myApplication.getMamapl().setLongitude(loc.getLongitude());
                myApplication.getMamapl().setLatitude(loc.getLatitude());
            }

            //
            String mPersId = StatusHolder.INSTANCE.getCurUserBean(getApplicationContext()).getPhone();
            List<OffLineLatLngInfo> item = DbHelper.getInstance().offLineLatLngInfoLocDBManager().queryBuilder()
                    .where(OffLineLatLngInfoDao.Properties.PersId.eq(mPersId))
                    .list();
            //上传判断
            if (item != null && NetUtil.hasNetwork(this) && item.size() > 0) {
                //大于100条，全部上传
                if (item.size() >= SIZE) {
                    uploadCountLocLatlng(item);
                }
                //小于100条根据时间判断上传  根据数据库中最旧的一条数据判断，5分钟时间节点
                Date currentLoc = null;
                try {
                    currentLoc = sdf1.parse(item.get(0).getOperateTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (currentLoc == null) {
                    currentLoc = new Date();
                }
                if (item.size() > 0 && (new Date().getTime() - currentLoc.getTime()) >= 5 * ONEMINIUE) {
                    uploadCountLocLatlng(item);
                }
            }

            if (newlatlng.latitude != 0.0f) {
                //先根据时间判断>1分钟后进行保存,一分钟以内的话进行判断距离和速度
                boolean flag = false;
                if ((new Date().getTime() - mCurrentMapLocation.getTime()) >= ONEMINIUE) {
                    flag = true;
                    last_latlng = newlatlng;
                    mCurrentMapLocation = new Date();
                } else {
                    if (loc.getSpeed() != 0 && IsDistanceMoreOneMile(last_latlng, newlatlng)) {
                        flag = true;
                        last_latlng = newlatlng;
                        mCurrentMapLocation = new Date();
                    }
                }

                if (item.size() == 0) {
                    //第一次的情况下
                    flag = true;
                    last_latlng = newlatlng;
                    mCurrentMapLocation = new Date();
                }
                if (flag) {
                    //存数据库
                    String adCode = "";
                    if (myApplication.getMamapl() != null) {
                        adCode = myApplication.getMamapl().getAdCode();
                        adCode = MapUtils.getCityCode(adCode);
                    }
                    UserBean userBean = StatusHolder.INSTANCE.getCurUserBean(getApplicationContext());
                    if (userBean == null) {
                        return;
                    }
                    OffLineLatLngInfo info = new OffLineLatLngInfo();
                    info.setAdCode(adCode);
                    info.setPersId(userBean.getPersid());
                    info.setPersName(userBean.getPersid() + "");
                    info.setLat(loc.getLatitude());
                    info.setLon(loc.getLongitude());
                    info.setOperateTime(sdf1.format(new Date().getTime()));
                    info.setMobile(userBean.getPhone() + "");
                    info.setAddress(JSDateUtil.getDataStringByObj(loc.getAddress()));
                    info.setSpeed(loc.getSpeed());
                    DbHelper.getInstance().offLineLatLngInfoLocDBManager().insert(info);
                    //写入文本测试
                    FileUtil.writeTxtToFile(info);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 批量上传
     */
    private void uploadCountLocLatlng(final List<OffLineLatLngInfo> item) {
        try {
            final List<OffLineLatLngInfo> itemS = item;
            //分离 以1_000条数据为基础
            if (itemS.size() <= SIZE) {
                //直接上传
                MapService1 service = RetrofitClient.INSTANCE.createService(MapService1.class);
                Call<CommonResponse> call = service.uploadPositionList(itemS);
                call.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if(response.body().getSuccess()){
                            //上传成功后删除
                        deleteCurrentLatlngList(itemS);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });
//                call.enqueue(new Callback<CommonResponse<Object>>() {
//                    @Override
//                    public void onResponse(@NotNull Call<CommonResponse<Object>> call, @NotNull Response<CommonResponse<Object>> response) {
//                        //上传成功后删除
//                        deleteCurrentLatlngList(itemS);
//                    }
//
//                    @Override
//                    public void onFailure(Call<CommonResponse<Object>> call, Throwable t) {
//
//                    }
//                });


            } else {
                int count = itemS.size() / SIZE;
                int yu = itemS.size() % SIZE;
                if (yu > 0) {
                    count += 1;
                }
                for (int i = 0; i < count; ++i) {
                    final int k = i;
                    ThreadPoolUtil.execute(() -> {
                        int sizeCan = (itemS.size() - SIZE * k) / SIZE > 0 ? SIZE : itemS.size() - SIZE * k;
                        List<OffLineLatLngInfo> itemQuery = itemS.subList(SIZE * k, SIZE * k + sizeCan);
                        uploadCountLocLatlng(itemQuery);
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除
     */
    private void deleteCurrentLatlngList(final List<OffLineLatLngInfo> item) {
        try {
            DbHelper.getInstance().offLineLatLngInfoLocDBManager().deleteInTx(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 判断两个点之间的距离大于规定的距离
     */
    private boolean IsDistanceMoreOneMile(LatLng last, LatLng news) {
        if (last == null) {
            return true;
        }
        float mi = AMapUtils.calculateLineDistance(last, news);
        if (mi >= 5.0f) {
            return true;
        } else
            return false;
    }

    /***
     * 判断当前时间是否在规定的时间段范围内
     * */
    public static boolean isCurrentInTimeScope() {
        int beginHour = 5;
        int beginMin = 0;
        int endHour = 23;
        int endMin = 0;
        boolean result = false;
        final long aDayInMillis = ONEMINIUE * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();
        Time now = new Time();
        now.set(currentTimeMillis);
        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;
        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;
        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }


}