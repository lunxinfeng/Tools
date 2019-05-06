package com.lxf.tools.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.lxf.rxpermissions.RxPermissions
import com.lxf.rxretrofit.RetrofitHelper
import com.lxf.rxretrofit.callback.BaseView
import com.lxf.rxretrofit.callback.ProgressObserver
import com.lxf.rxretrofit.download
import com.lxf.rxretrofit.transformer.Transformer
import com.lxf.tools.ApiService
import com.lxf.tools.R
import com.lxf.tools.bean.*
import com.lxf.tools.net_hint.BaseActivity
import com.lxf.tools.util.getSDPath
import com.lxf.tools.util.io_main_izis
import com.lxf.tools.util.toast
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_retrofit.*
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.util.HashMap

class RetrofitActivity : BaseActivity() {
    private val TAG = "RetrofitActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)

        init()

        btnObject.setOnClickListener { getVersion() }
        btnMap.setOnClickListener { map() }
        btnDownload.setOnClickListener { permission() }
        btnPost.setOnClickListener { post() }
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
//                .compose(Transformer.io_main<ResultEntity<Map<String, String>>>())
                .subscribeOn(Schedulers.io())
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

    private fun permission(){
        RxPermissions.newInstance(this)
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .subscribe { if (it) download() else toast("权限被拒绝") }

    }

    private fun download(){
        RetrofitHelper.newInstance()
                .debug(true)
                .download<BaseView>(
                        tag = "download",
                        url = "http://www.izis.cn/GoWebService/yztv_10.apk",
                        filePath = getSDPath() + File.separator + "yztv.apk",
                        progressListener = {_, _, progress, done ->
                            Log.d(TAG,"download：$progress\t$done")
                            progressBar.progress = progress
                        },
                        completeListener = {
                            Log.d(TAG,"download complete：complete")
                            toast("complete")
                        },
                        errorListener = {
                            Log.d(TAG,"download error：$it")
                            toast(it)
                        }
                )
    }

    private fun post(){
        RetrofitHelper.getInstance()
                .baseUrl("http://apitest.yqlwq.cn")
                .create(ApiService::class.java)
                .login(HashMap<String,String>().apply {
                    this["phone"] = "15116480723"
                    this["password"] = "123456"
                })
//                .login("15116480723", "123456")
                .subscribeOn(Schedulers.io())
                .subscribe(object : ProgressObserver<Response<ResponseBody>,BaseView>(){
                    override fun doOnError(errorMessage: String) {
                        Log.d(TAG, errorMessage)
                    }

                    override fun doOnNext(data: Response<ResponseBody>) {
                        Log.d(TAG, data.toString())
                    }
                })
    }

    override fun onBackPressed() {
        if (RetrofitHelper.hasTask("download"))
            RetrofitHelper.cancel("download")
        else
            super.onBackPressed()
    }
}
