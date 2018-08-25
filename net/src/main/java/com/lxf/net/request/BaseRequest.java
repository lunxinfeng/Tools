package com.lxf.net.request;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lxf.net.RetrofitHelper;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public abstract class BaseRequest<R extends BaseRequest,A> {
    private String url;
    private A apiService;

    public BaseRequest(String url) {
        this.url = url;
    }

    private OkHttpClient.Builder generateOkClient() {
        return RetrofitHelper.getOkHttpClientBuilder();
    }
    private Retrofit.Builder generateRetrofit() {
        return RetrofitHelper.getRetrofitBuilder();
    }
    protected R build() {
        OkHttpClient.Builder okHttpClientBuilder = generateOkClient();
        Retrofit.Builder retrofitBuilder = generateRetrofit();
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());//增加RxJava2CallAdapterFactory
        OkHttpClient okHttpClient = okHttpClientBuilder.build();
        retrofitBuilder.client(okHttpClient);
        Retrofit retrofit = retrofitBuilder.build();
        apiService = retrofit.create(Class.forName(A));
        return (R) this;
    }

    protected abstract Observable<ResponseBody> generateRequest();
}
