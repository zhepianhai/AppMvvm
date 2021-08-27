package com.gw.safty.common.glide;

import android.content.Context;

import com.gw.safty.common.network.SecureNetworktConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class UnsafeOkHttpsClient {
    public static OkHttpClient getUnsafeOkHttpClient(Context context) {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            try {
                InputStream keyInputstream = context.getAssets().open("jinshui.bks");
                SecureNetworktConfig.SSLParams sslSocketFactory = SecureNetworktConfig.getSslSocketFactory(null, keyInputstream, "123456");
                builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory, sslSocketFactory.trustManager);
                builder.hostnameVerifier((s, sslSession) -> true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            builder.connectTimeout(20, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
