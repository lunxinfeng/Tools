package com.lxf.rxretrofit.config

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.lxf.rxretrofit.converter.NullOnEmptyConverterFactory
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


internal class RetrofitConfig private constructor() {
    internal var isCustomClient = false
    private var isCustomRetrofitConverter = false
    private var isCustomRetrofitAdapter = false

    init {
        retrofitBuilder = Retrofit.Builder()
    }

    companion object {
        @Volatile
        private var instance: RetrofitConfig? = null
        private lateinit var retrofitBuilder: Retrofit.Builder

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance
                            ?: RetrofitConfig().apply { instance = this }
                }

        fun newInstance() = RetrofitConfig()
    }

    internal fun addCallAdapterFactory(factory: CallAdapter.Factory): RetrofitConfig {
        retrofitBuilder.addCallAdapterFactory(factory)
        isCustomRetrofitAdapter = true
        return this
    }

    internal fun addConverterFactory(factory: Converter.Factory): RetrofitConfig {
        retrofitBuilder.addConverterFactory(factory)
        isCustomRetrofitConverter = true
        return this
    }

    internal fun client(client: OkHttpClient): RetrofitConfig {
        isCustomClient = true
        retrofitBuilder.client(client)
        return this
    }

    internal fun baseUrl(url: String): RetrofitConfig {
        retrofitBuilder.baseUrl(url)
        return this
    }

    internal fun build(): Retrofit {
        if (!isCustomRetrofitAdapter) {
            retrofitBuilder
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }

        if (!isCustomRetrofitConverter) {
            retrofitBuilder
                    .addConverterFactory(NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
        }
        return retrofitBuilder.build()
    }

    internal fun <T> create(service: Class<T>) = build().create(service)
}