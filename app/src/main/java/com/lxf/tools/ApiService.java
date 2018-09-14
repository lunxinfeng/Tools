package com.lxf.tools;

import com.lxf.tools.bean.RequestModel;
import com.lxf.tools.bean.ResponseModel;
import com.lxf.tools.bean.ResultEntity;
import com.lxf.tools.bean.SignRequestBody;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("getdataserver")
    Observable<ResponseModel<String>>  getVersion(@Body RequestModel requestModel);
    @POST("postdataserver")
    Observable<ResponseModel> post(@Body RequestModel requestModel);



    @POST("/api/terminal/v1/merchant/newlogin")
    Observable<ResultEntity<Map<String, String>>> login(
            @Body SignRequestBody body
    );
}
