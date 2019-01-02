package com.lxf.rxretrofit.exception

//import android.net.ParseException
//import android.os.NetworkOnMainThreadException
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializer
//import org.apache.http.conn.ConnectTimeoutException
//import org.json.JSONException
import retrofit2.HttpException
import java.io.NotSerializableException
import java.net.ConnectException
import java.net.UnknownHostException


class ApiException private constructor(throwable: Throwable, val code: Int) : Exception(throwable) {
    internal var errorInfo: String? = throwable.message


    /**
     * 约定异常
     */
    object ERROR {
        /**
         * 未知错误
         */
        val UNKNOWN = 1000
        /**
         * 解析错误
         */
        val PARSE_ERROR = UNKNOWN + 1
        /**
         * 网络错误
         */
        val NETWORD_ERROR = PARSE_ERROR + 1
        /**
         * 协议出错
         */
        val HTTP_ERROR = NETWORD_ERROR + 1

        /**
         * 证书出错
         */
        val SSL_ERROR = HTTP_ERROR + 1

        /**
         * 连接超时
         */
        val TIMEOUT_ERROR = SSL_ERROR + 1

        /**
         * 调用错误
         */
        val INVOKE_ERROR = TIMEOUT_ERROR + 1
        /**
         * 类转换错误
         */
        val CAST_ERROR = INVOKE_ERROR + 1
        /**
         * 请求取消
         */
        val REQUEST_CANCEL = CAST_ERROR + 1
        /**
         * 未知主机错误
         */
        val UNKNOWNHOST_ERROR = REQUEST_CANCEL + 1

        /**
         * 空指针错误
         */
        val NULLPOINTER_EXCEPTION = UNKNOWNHOST_ERROR + 1
    }

    companion object {

        fun handleException(e: Throwable): ApiException {
            val ex: ApiException
            if (e is HttpException) {
                ex = ApiException(e, e.code())
                ex.errorInfo = e.message
                return ex
            } else if (e is ServerException) {
                ex = ApiException(e, e.errCode)
                ex.errorInfo = e.message
                return ex
            } else if (e is JsonParseException || e is JsonSerializer<*> || e is NotSerializableException) {
                ex = ApiException(e, ERROR.PARSE_ERROR)
                ex.errorInfo = "解析错误"
                return ex
            } else if (e is ClassCastException) {
                ex = ApiException(e, ERROR.CAST_ERROR)
                ex.errorInfo = "类型转换错误"
                return ex
            } else if (e is ConnectException) {
                ex = ApiException(e, ERROR.NETWORD_ERROR)
                ex.errorInfo = "连接失败"
                return ex
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                ex = ApiException(e, ERROR.SSL_ERROR)
                ex.errorInfo = "证书验证失败"
                return ex
            }
//            else if (e is ConnectTimeoutException) {
//                ex = ApiException(e, ERROR.TIMEOUT_ERROR)
//                ex.errorInfo = "连接超时"
//                return ex
//            }
            else if (e is java.net.SocketTimeoutException) {
                ex = ApiException(e, ERROR.TIMEOUT_ERROR)
                ex.errorInfo = "连接超时"
                return ex
            } else if (e is UnknownHostException) {
                ex = ApiException(e, ERROR.UNKNOWNHOST_ERROR)
                ex.errorInfo = "无法解析该域名"
                return ex
            } else if (e is NullPointerException) {
                ex = ApiException(e, ERROR.NULLPOINTER_EXCEPTION)
                ex.errorInfo = "NullPointerException"
                return ex
            }
//            else if (e is NetworkOnMainThreadException) {
//                ex = ApiException(e, ERROR.NULLPOINTER_EXCEPTION)
//                ex.errorInfo = "不能在主线程请求网络"
//                return ex
//            }
            else {
                ex = ApiException(e, ERROR.UNKNOWN)
                ex.errorInfo = "未知错误：${e.message}"
                return ex
            }
        }
    }
}