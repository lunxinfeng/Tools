package com.lxf.net;

import android.support.annotation.NonNull;

import com.lxf.net.https.HttpsUtils;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public final class NetWork {
    private static final int DEFAULT_MILLISECONDS = 60 * 1000;

    private volatile static NetWork singleton = null;
    private OkHttpClient.Builder okHttpClientBuilder;
    private Retrofit.Builder retrofitBuilder;
    private String baseUrl;

    private NetWork() {
        okHttpClientBuilder = new OkHttpClient.Builder()
                .hostnameVerifier(new DefaultHostnameVerifier())
                .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        retrofitBuilder = new Retrofit.Builder();
    }

    public static NetWork getInstance() {
        if (singleton == null) {
            synchronized (NetWork.class) {
                if (singleton == null) {
                    singleton = new NetWork();
                }
            }
        }
        return singleton;
    }

    /**
     * 是否开启日志
     */
    public NetWork debug(boolean debug){
        if (debug){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
        }
        return this;
    }

    /**
     * 全局读取超时时间
     */
    public NetWork setReadTimeOut(long readTimeOut) {
        okHttpClientBuilder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 全局写入超时时间
     */
    public NetWork setWriteTimeOut(long writeTimeout) {
        okHttpClientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 全局连接超时时间
     */
    public NetWork setConnectTimeout(long connectTimeout) {
        okHttpClientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 全局设置baseurl
     */
    public NetWork setBaseUrl(@NonNull String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * 添加全局拦截器
     */
    public NetWork addInterceptor(@NonNull Interceptor interceptor) {
        okHttpClientBuilder.addInterceptor(interceptor);
        return this;
    }

    /**
     * 添加全局网络拦截器
     */
    public NetWork addNetworkInterceptor(@NonNull Interceptor interceptor) {
        okHttpClientBuilder.addNetworkInterceptor(interceptor);
        return this;
    }

    /**
     * https的全局访问规则
     */
    public NetWork setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        okHttpClientBuilder.hostnameVerifier(hostnameVerifier);
        return this;
    }

    /**
     * https的全局自签名证书
     */
    public NetWork setCertificates(InputStream... certificates) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, certificates);
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /**
     * https双向认证证书
     */
    public NetWork setCertificates(InputStream bksFile, String password, InputStream... certificates) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, certificates);
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }


    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        return getInstance().okHttpClientBuilder;
    }

    public static Retrofit.Builder getRetrofitBuilder() {
        return getInstance().retrofitBuilder;
    }
}
