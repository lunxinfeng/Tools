package com.lxf.tools.ui

import android.os.Bundle
import android.util.Log
import com.lxf.rxretrofit.RetrofitHelper
import com.lxf.rxretrofit.callback.BaseView
import com.lxf.rxretrofit.callback.ProgressObserver
import com.lxf.rxretrofit.transformer.Transformer
import com.lxf.tools.ApiService
import com.lxf.tools.R
import com.lxf.tools.bean.*
import com.lxf.tools.net_hint.BaseActivity
import com.lxf.tools.util.io_main_izis
import kotlinx.android.synthetic.main.activity_retrofit.*
import java.util.HashMap

class RetrofitActivity : BaseActivity() {
    private val TAG = "RetrofitActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)

        init()

        btnObject.setOnClickListener { getVersion() }
        btnMap.setOnClickListener { map() }
    }

    private fun init(){
        RetrofitHelper.getInstance()
                .debug(true)
    }

    private fun getVersion(){
        RetrofitHelper.getInstance()
                .baseUrl("http://app.izis.cn/GoWebService/")
                .create(ApiService::class.java)
                .getVersion(RequestModel().apply {
                    code = "060101"
                    info = """
                        {"root":[{"type":"3"}]}
                    """.trimIndent()
                    snum = "xxxx"
                    userid = 0
                })
//                .compose(Transformer.io_main<ResponseModel<String>>())
                .compose(Transformer.io_main_izis(VersionMessage::class.java))
                .subscribe(object : ProgressObserver<VersionMessage, BaseView>() {
                    override fun doOnError(e: Throwable, errorMessage: String) {
                        super.doOnError(e, errorMessage)
                        e.printStackTrace()
                    }
                    override fun doOnError(errorMessage: String) {
                        Log.d(TAG,"doOnError：$errorMessage")
                    }

                    override fun doOnNext(data: VersionMessage) {
                        Log.d(TAG,"doOnNext：$data")
                    }
                })
    }

    private fun map(){
        RetrofitHelper.getInstance()
                .baseUrl("http://api.trueinfo.cn")
                .create(ApiService::class.java)
                .login(SignRequestBody(
                        HashMap<String, String>().apply {
                            this["userName"] = "13875901966@001"
                            this["password"] = "a12345678"
                            this["app_version"] = "2"
                        }
                ))
                .compose(Transformer.io_main<ResultEntity<Map<String, String>>>())
                .subscribe(object : ProgressObserver<ResultEntity<Map<String, String>>, BaseView>() {
                    override fun doOnError(e: Throwable, errorMessage: String) {
                        super.doOnError(e, errorMessage)
                        e.printStackTrace()
                    }
                    override fun doOnError(errorMessage: String) {
                        Log.d(TAG,"doOnError：$errorMessage")
                    }

                    override fun doOnNext(data: ResultEntity<Map<String, String>>) {
                        Log.d(TAG,"doOnNext：$data")
                    }
                })
    }
}
