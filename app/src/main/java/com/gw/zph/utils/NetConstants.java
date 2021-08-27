package com.gw.zph.utils;

import com.gw.zph.BuildConfig;

public class NetConstants {

// 开发环境
//      public final static String API_APP_BASE_URL_SECOND = "http://10.1.102.136:8100/";//内网测试 主URL
//    public final static String API_APP_BASE_URL_SECOND = "http://10.1.102.169:8100/";//玉镯主URL
//    public final static String API_APP_BASE_URL_SECOND = "http://192.168.0.119:8011/";//赵舔龙主URL
// 生产环境
//    public final static String API_APP_BASE_URL_SECOND = "http://slbdc-pc.goldenwater.com.cn/";//外网 水利部演示
    public final static String API_APP_BASE_URL_SECOND = BuildConfig.BASE_URL + (BuildConfig.appendApi? "api/" : "") ;//外网 正式环境 https版本 2020-2-8
    public final static boolean IS_HAVE_API = false;
    public final static String API_APP_FILE_URL = API_APP_BASE_URL_SECOND;
    public final static String API_H5_BASE_URL = API_APP_BASE_URL_SECOND;

}

