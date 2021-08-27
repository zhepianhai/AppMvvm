package com.gw.safty.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by dell on 2016/8/9.
 */
public class NetUtil {

    public static boolean hasNetwork(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public synchronized static boolean isConnect(String url){
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection conn=(HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(500);//连接主机时间，避免卡住
            conn.setReadTimeout(500);//从主机读取时间，避免卡住
            conn.setRequestMethod("GET");
            System.out.println(conn.getResponseCode());//测试
            return (conn.getResponseCode()==HttpURLConnection.HTTP_OK);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}
