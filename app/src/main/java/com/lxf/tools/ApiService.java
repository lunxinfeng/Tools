package com.lxf.tools;

import com.lxf.tools.bean.RequestModel;
import com.lxf.tools.bean.ResponseModel;
import com.lxf.tools.bean.ResultEntity;
import com.lxf.tools.bean.SignRequestBody;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("getdataserver")
    Observable<ResponseModel<String>>  getVersion(@Body RequestModel requestModel);
    @POST("postdataserver")
    Observable<ResponseModel> post(@Body RequestModel requestModel);



    @POST("/api/terminal/v1/merchant/newlogin")
    Observable<ResultEntity<Map<String, String>>> login(
            @Body SignRequestBody body
    );


    @POST("api/game/user/login")
//    Observable<ResponseBody> login(@Query("phone") String phone, @Query("password") String password);
    Observable<Response<ResponseBody>> login(@Body Map<String,String> params);
}
