package com.lxf.rxretrofit.api

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.*


internal interface ApiService {

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>

    @Multipart
    @POST
    fun uploadFiles(
            @Url uploadUrl: String,
            @Part files: List<MultipartBody.Part>
    ): Observable<ResponseBody>
}