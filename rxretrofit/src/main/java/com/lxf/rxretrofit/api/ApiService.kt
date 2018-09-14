package com.lxf.rxretrofit.api

import com.lxf.rxretrofit.download.DownloadResponseBody
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


internal interface ApiService {

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<DownloadResponseBody>
}