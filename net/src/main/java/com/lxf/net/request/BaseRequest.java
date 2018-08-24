package com.lxf.net.request;


import com.lxf.net.NetWork;

public class BaseRequest<R extends BaseRequest> {
    private String url;

    public BaseRequest(String url) {
        this.url = url;
    }

    public R build(){
//        NetWork.getRetrofitBuilder().build().create();
        return (R) this;
    }
}
