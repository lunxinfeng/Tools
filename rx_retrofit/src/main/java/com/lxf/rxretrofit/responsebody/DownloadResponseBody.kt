package com.lxf.rxretrofit.responsebody

import com.lxf.rxretrofit.callback.ProgressListener
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Okio
import java.io.IOException


class DownloadResponseBody(
        private val originalResponseBody: ResponseBody,
        private val progressListener: ProgressListener? = null
) : ResponseBody() {
    override fun contentLength() = originalResponseBody.contentLength()

    override fun contentType() = originalResponseBody.contentType()

    override fun source(): BufferedSource {
        return Okio.buffer(object : ForwardingSource(originalResponseBody.source()) {
            var bytesReaded: Long = 0

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                bytesReaded += if (bytesRead == -1L) 0 else bytesRead
                //发布进度信息
                progressListener?.invoke(
                        bytesReaded,
                        originalResponseBody.contentLength(),
                        Math.floor(bytesReaded / (originalResponseBody.contentLength().toDouble()) * 100).toInt(),
                        bytesRead == -1L
                )
                return bytesRead
            }
        })
    }
}